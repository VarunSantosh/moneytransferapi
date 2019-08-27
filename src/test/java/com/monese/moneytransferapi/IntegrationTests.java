package com.monese.moneytransferapi;

import com.monese.moneytransferapi.model.Account;
import com.monese.moneytransferapi.model.AccountTransferPair;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class IntegrationTests {

	@Autowired
	private WebTestClient webClient;

	/**
	 * This test is to check the successful call to get the account statement for an existing account
	 */
	@Test
	void testAccountStatementApi() {
		this.webClient.get().uri("/v1/account/1/statement").exchange()
				.expectStatus().isOk()
				.expectBody(Account.class)
				.consumeWith(response ->
				{
					Assertions.assertThat(response.getResponseBody()).isNotNull();
					Assert.assertEquals(response.getResponseBody().getId(), new Long(1));
					Assert.assertEquals(response.getResponseBody().getFirstName(), "VARUN");
					Assert.assertEquals(response.getResponseBody().getLastName(), "DAMANI");
					Assert.assertThat(response.getResponseBody().getBalance(), Matchers.comparesEqualTo(new BigDecimal(5000.00)));
					Assert.assertEquals(response.getResponseBody().getTransactions().size(), 0);
				});
	}

	/**
	 * This test is to check the successful transfer of money from valid account with valid balance
	 */
	@Test
	void testTransferApi() {
		this.webClient.post().uri("/v1/account/1/transfer/2/500").exchange()
				.expectStatus().isCreated()
				.expectBody(AccountTransferPair.class)
				.consumeWith(response ->
				{
					Assertions.assertThat(response.getResponseBody()).isNotNull();
					Assert.assertTrue(response.getResponseBody().getId() > 0);
					Assert.assertEquals(response.getResponseBody().getFromAccount(), new Long(1));
					Assert.assertEquals(response.getResponseBody().getToAccount(), new Long(2));
					Assert.assertThat(response.getResponseBody().getAmount(), Matchers.comparesEqualTo(new BigDecimal(500.00)));
				});

		this.webClient.get().uri("/v1/account/1/statement").exchange()
				.expectStatus().isOk()
				.expectBody(Account.class)
				.consumeWith(response ->
				{
					Assertions.assertThat(response.getResponseBody()).isNotNull();
					Assert.assertEquals(response.getResponseBody().getId(), new Long(1));
					Assert.assertEquals(response.getResponseBody().getFirstName(), "VARUN");
					Assert.assertEquals(response.getResponseBody().getLastName(), "DAMANI");
					Assert.assertThat(response.getResponseBody().getBalance(), Matchers.comparesEqualTo(new BigDecimal(4500.00)));
					Assert.assertEquals(response.getResponseBody().getTransactions().size(), 1);
				});

		this.webClient.get().uri("/v1/account/2/statement").exchange()
				.expectStatus().isOk()
				.expectBody(Account.class)
				.consumeWith(response ->
				{
					Assertions.assertThat(response.getResponseBody()).isNotNull();
					Assert.assertEquals(response.getResponseBody().getId(), new Long(2));
					Assert.assertEquals(response.getResponseBody().getFirstName(), "SALMAN");
					Assert.assertEquals(response.getResponseBody().getLastName(), "KHAN");
					Assert.assertThat(response.getResponseBody().getBalance(), Matchers.comparesEqualTo(new BigDecimal(50500.00)));
					Assert.assertEquals(response.getResponseBody().getTransactions().size(), 1);
				});
	}

	/**
	 * This test is to check for transfer of money failure because of insufficient balance in source account
	 */
	@Test
	void testTransferApiToFail() {
		this.webClient.post().uri("/v1/account/1/transfer/2/6000").exchange()
				.expectStatus().isBadRequest();
	}
}