package com.zufar.onlinestore.product.endpoint;

import com.zufar.onlinestore.product.util.PaginationAndSortingAttribute;
import com.zufar.onlinestore.test.config.AbstractE2ETest;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static com.zufar.onlinestore.test.config.RestAssertion.*;
import static io.restassured.RestAssured.given;


@Testcontainers
@DisplayName("ProductsEndpoint Tests")
class ProductsEndpointTest extends AbstractE2ETest {

    private static final String PRODUCT_SCHEMA_LOCATION = "product/model/schema/product-schema.json";
    private static final String PRODUCT_FAILED_SCHEMA_LOCATION = "product/model/schema/product-failed-schema.json";
    private static final String PRODUCT_LIST_PAGINATION_SCHEMA_LOCATION = "product/model/schema/product-list-pagination-schema.json";

    private static final String NAME_ATTRIBUTE = "name";

    private static final String AMERICAN_PRODUCT_NAME = "Americano";

    private static final String PRODUCTS_PATH_TO_NAME = "products.name";

    @Test
    @DisplayName("Get product by exists ID")
    void shouldRetrieveProductSuccessfullyById() {
        String productId = "418499f3-d951-40bf-9414-5cb90ab21ecb";
        Response response = given(specification)
                .get(ProductsEndpoint.GET_SINGLE_PRODUCT_PATH, productId);
        assertRestApiBodySchemaResponse(response, HttpStatus.OK.value(), PRODUCT_SCHEMA_LOCATION);
    }

    @Test
    @DisplayName("Get product by doesn't exists ID")
    void shouldReturnNotFoundForInvalidProductId() {
        String invalidProductId = UUID.randomUUID().toString();
        Response response = given(specification)
                .get(ProductsEndpoint.GET_SINGLE_PRODUCT_PATH, invalidProductId);
        assertRestApiNotFoundResponse(response, PRODUCT_FAILED_SCHEMA_LOCATION);
    }

    @Test
    @DisplayName("Fetch products with pagination and sorting")
    void shouldFetchProductsWithPaginationAndSorting() {
        Map<String, Object> params = new HashMap<>();
        params.put(PaginationAndSortingAttribute.PAGE, 1);
        params.put(PaginationAndSortingAttribute.SIZE, 1);
        params.put(PaginationAndSortingAttribute.SORT_ATTRIBUTE, NAME_ATTRIBUTE);
        params.put(PaginationAndSortingAttribute.SORT_DIRECTION, Sort.Direction.DESC.name().toLowerCase());
        Response response = given(specification)
                .queryParams(params)
                .get();
        assertRestApiBodySchemaResponse(response, HttpStatus.OK.value(), PRODUCT_LIST_PAGINATION_SCHEMA_LOCATION);
    }

    @Test
    @DisplayName("Fetch products with invalid pagination and sorting attributes")
    void shouldReturnErrorForInvalidSortAttribute() {
        Map<String, Object> params = new HashMap<>();
        params.put(PaginationAndSortingAttribute.PAGE, 15);
        params.put(PaginationAndSortingAttribute.SIZE, 8);
        params.put(PaginationAndSortingAttribute.SORT_ATTRIBUTE, NAME_ATTRIBUTE + new Random().nextInt());
        params.put(PaginationAndSortingAttribute.SORT_DIRECTION, Sort.Direction.DESC.name().toLowerCase());
        Response response = given(specification)
                .queryParams(params)
                .get();
        assertRestApiEmptyBodyResponse(response, HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("Get product with name `Americano`")
    void shouldContainProductWithNameAmericano() {
        Map<String, Object> params = new HashMap<>();
        params.put(PaginationAndSortingAttribute.PAGE, 5);
        params.put(PaginationAndSortingAttribute.SIZE, 1);
        params.put(PaginationAndSortingAttribute.SORT_ATTRIBUTE, NAME_ATTRIBUTE);
        params.put(PaginationAndSortingAttribute.SORT_DIRECTION, Sort.Direction.DESC.name().toLowerCase());
        Response response = given(specification)
                .queryParams(params)
                .get();
        assertRestApiOkResponse(
                response,
                PRODUCTS_PATH_TO_NAME,
                AMERICAN_PRODUCT_NAME,
                PRODUCT_LIST_PAGINATION_SCHEMA_LOCATION
        );
    }
}
