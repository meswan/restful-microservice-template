/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package it.io.openliberty.rest;

import io.openliberty.rest.BookApplication;
import io.openliberty.rest.BookResource;

import java.util.Collection;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.microshed.testing.jaxrs.RESTClient;
import org.microshed.testing.jupiter.MicroShedTest;
import org.microshed.testing.SharedContainerConfig;
import org.microshed.testing.jaxrs.RESTClient;

@MicroShedTest
@SharedContainerConfig(EndpointSharedApplication.class)
@TestMethodOrder(OrderAnnotation.class)
public class EndpointMicroshedIT {

    @RESTClient
    public static BookResource bookrsc;
    
    @Test
    @DisplayName("GET - Verifying initial state of database")
    @Order(1)
    public void CheckIfInitialDatabaseIsNotEmpty() {

        assertNotNull(bookrsc.listBooks());
    }

    @Test
    @DisplayName("POST - Updating author's name in book with id 123")
    @Order(2)
    public void UpdateBook() {

        BookApplication book = new BookApplication("123", "The fall of the Emperor", "Nathan T Gold", "The story of a great Emperor");
        bookrsc.updateBook("123", book);
        Collection<BookApplication> bookCollection = bookrsc.listBooks();
        for (BookApplication bookApp: bookCollection){
            if (bookApp.getId().equals("123")){
                assertEquals("Nathan T Gold", bookApp.getAuthor(), "Database does not reflect updated book");
            }
        }
    }

    @Test
    @DisplayName("DELETE - Deleting book with id 123")
    @Order(3)
    public void DeleteBook() {

        assertNotNull(bookrsc.takeBook("123"));

        Collection<BookApplication> bookCollection = bookrsc.listBooks();

        for (BookApplication bookApp: bookCollection) {
            assertFalse(bookApp.getId().equals("123"), "Database does not reflect deleted book");
        }   
    }

    @Test
    @DisplayName("PUT - Adding book with id 123")
    @Order(4)
    public void addBook() {

        BookApplication book = new BookApplication("123", "The fall of the Emperor", "Nathan T", "The story of a great Emperor");
        bookrsc.depositBook(book);
        boolean bookFound = false;

        Collection<BookApplication> bookCollection = bookrsc.listBooks();
        for (BookApplication bookApp: bookCollection){
            if (bookApp.getId().equals("123")){
                bookFound = true;
            }
        }
        assertTrue(bookFound, "Database does not reflect new book");
    }
}
