package com.book.service;

import com.book.service.repo.Book;
import com.book.service.repo.BookRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(value = {"/schema-test.sql", "/data-test.sql"}, executionPhase = BEFORE_TEST_METHOD)
public class BookControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private BookRepository repository;

	@Test
	public void whenPerformDelete_ThenBookIsDeleted() throws Exception {
		//Given an existing book
		Optional<Book> bookOptional = repository.findById(7L);
		Assert.assertTrue(bookOptional.isPresent());
		//When
		mockMvc.perform(delete("/books/7")).andExpect(status().isOk());
		//Then
		Optional<Book> bookDeleted = repository.findById(7L);
		Assert.assertFalse(bookDeleted.isPresent());
	}

	@Test
	public void givenBookWhenPerformPostThenBookIsCreated() throws Exception {
		//Given an existing book
		String bookJson = "{" +
				"\"title\": \"Mock title\"," +
				"\"author\":\"Mock author\"," +
				"\"category\": \"Mock category\", " +
				"\"publishedDate\": \"2020-01-01\"" +
				"}";
		//When
		mockMvc.perform(post("/books")
				.contentType(APPLICATION_JSON)
				.content(bookJson))
				.andExpect(status().isCreated());
		//Then
		Iterable<Book> books = repository.findAll();
		assertThat(books).extracting(Book::getTitle).contains("Mock title");
		assertThat(books).extracting(Book::getAuthor).contains("Mock author");
		assertThat(books).extracting(Book::getCategory).contains("Mock category");
		assertThat(books).extracting(Book::getPublishedDate).contains(LocalDate.of(2020, 1, 1));
	}

	@Test
	public void givenBookWithPublishYearOnlyWhenPerformPostThenBookIsCreated() throws Exception {
		//Given an existing book
		String bookJson = "{" +
				"\"title\": \"Mock title\"," +
				"\"author\":\"Mock author\"," +
				"\"category\": \"Mock category\", " +
				"\"publishedDate\": \"1999\"" +
				"}";
		//When
		mockMvc.perform(post("/books")
				.contentType(APPLICATION_JSON)
				.content(bookJson))
				.andExpect(status().isCreated());
		//Then
		Iterable<Book> books = repository.findAll();
		assertThat(books).extracting(Book::getTitle).contains("Mock title");
		assertThat(books).extracting(Book::getAuthor).contains("Mock author");
		assertThat(books).extracting(Book::getCategory).contains("Mock category");
		assertThat(books).extracting(Book::getPublishedDate).contains(LocalDate.of(1999, 1, 1));
	}

	@Test
	public void givenBookWithoutTitleWhenPerformPostThenFieldErrorIsReturned() throws Exception {
		//Given book without title
		String bookJson = "{" +
				"\"title\": \"\"," +
				"\"author\":\"Mock author\"," +
				"\"category\": \"Mock category\", " +
				"\"publishedDate\": \"2020-01-01\"" +
				"}";
		//When
		ResultActions postBooksResponse = mockMvc.perform(post("/books")
				.contentType(APPLICATION_JSON)
				.content(bookJson));
		//Then
		postBooksResponse
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.title", is("must not be blank")));
	}

	@Test
	public void givenBookWithInvalidDateFormatWhenPerformPostThenErrorIsReturned() throws Exception {
		//Given book with invalid date
		String bookJson = "{" +
				"\"title\": \"Mock title\"," +
				"\"author\":\"Mock author\"," +
				"\"category\": \"Mock category\", " +
				"\"publishedDate\": \"01/01/2000\"" +
				"}";
		//When
		ResultActions postBooksResponse = mockMvc.perform(post("/books")
				.contentType(APPLICATION_JSON)
				.content(bookJson));
		//Then
		postBooksResponse
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", containsString("Invalid date or year: 01/01/2000")));
	}

	@Test
	public void givenBookWithInvalidYearWhenPerformPostThenErrorIsReturned() throws Exception {
		//Given book with invalid year
		String bookJson = "{" +
				"\"title\": \"Mock title\"," +
				"\"author\":\"Mock author\"," +
				"\"category\": \"Mock category\", " +
				"\"publishedDate\": \"12/1\"" +
				"}";
		//When
		ResultActions postBooksResponse = mockMvc.perform(post("/books")
				.contentType(APPLICATION_JSON)
				.content(bookJson));
		//Then
		postBooksResponse
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", containsString("Invalid date or year: 12/1")));
	}

	@Test
	public void whenPerformSaveBooksWithExistentTitle_ThenErrorIsReturned() throws Exception {
		//Given
		String bookJson = "{" +
				"\"title\": \"ReactJS Blueprints\"," +
				"\"author\":\"Mock author\"," +
				"\"category\": \"Mock category\", " +
				"\"publishedDate\": \"2000\"" +
				"}";
		//When
		ResultActions postBooksResponse = mockMvc.perform(post("/books")
				.contentType(APPLICATION_JSON)
				.content(bookJson));
		//Then
		postBooksResponse
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", is("Title already exists: ReactJS Blueprints")));
	}
}