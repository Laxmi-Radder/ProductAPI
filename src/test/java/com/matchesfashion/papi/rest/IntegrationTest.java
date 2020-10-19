package com.matchesfashion.papi.rest;

import com.matchesfashion.papi.PapiApplication;
import com.matchesfashion.papi.domain.Product;
import com.matchesfashion.papi.repository.ProductRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Integration tests
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PapiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @MockBean
    ProductRepository productRepository;

    @LocalServerPort
    private int port;

    TestRestTemplate testRestTemplate = new TestRestTemplate();

    List<Product> products;


    @Test
    public void testGetAllProducts() {
        ParameterizedTypeReference<List<Product>> responseType = new ParameterizedTypeReference<List<Product>>(){};
        ResponseEntity<List<Product>> allProducts = testRestTemplate.exchange(getUrl()+"/v1/all-products", HttpMethod.GET, null, responseType);
        Assert.assertNotNull(allProducts);
        Assert.assertEquals(HttpStatus.OK, allProducts.getStatusCode());
        Assert.assertNotNull(allProducts.getBody());
        Assert.assertEquals(2, allProducts.getBody().size());
        Assert.assertEquals(products.get(0).toString(), allProducts.getBody().get(0).toString());
    }

   @Test
    public void testGetAllProductsWithPriceGreaterThan() {
       UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(getUrl()).path("/v1/products").queryParam("price", 100);
       ParameterizedTypeReference<List<Product>> responseType = new ParameterizedTypeReference<List<Product>>(){};
       ResponseEntity<List<Product>> allProducts = testRestTemplate.exchange(urlBuilder.build().toString(), HttpMethod.GET, null, responseType);
       Assert.assertNotNull(allProducts);
       Assert.assertEquals(HttpStatus.OK, allProducts.getStatusCode());
       Assert.assertNotNull(allProducts.getBody());
       Assert.assertEquals(1, allProducts.getBody().size());
       //returns only second object with price 200
       Assert.assertEquals(products.get(1).toString(), allProducts.getBody().get(0).toString());
    }

    private String getUrl(){
        return "http://localhost:"+port;
    }

    @Before
    public void setUpProducts() {
        //mock response
        products = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(1);
        product1.setPrice(100);
        product1.setTitle("Product1");
        products.add(product1);
        Product product2 = new Product();
        product2.setId(2);
        product2.setPrice(200);
        product2.setTitle("Product2");
        products.add(product2);
        Mockito.when(productRepository.findAll()).thenReturn(products);
        Mockito.when(productRepository.findByPriceGreaterThan(100)).thenReturn(products.subList(1, products.size()));
    }

    @After
    public void cleanUp() {
        this.products = null;
    }
}
