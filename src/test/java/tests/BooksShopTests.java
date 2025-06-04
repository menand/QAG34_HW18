package tests;

import api.AuthAPI;
import api.BooksAPI;
import com.codeborne.selenide.Selenide;
import extensions.WithLogin;
import models.BookModel;
import models.LoginResponseModel;
import models.UserBooksResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.BOOKISBN;
import static tests.TestData.USERNAME;

public class BooksShopTests extends TestBase {

    @Test
    @WithLogin
    @DisplayName("Удаление книги из профиля: проверка через API и UI")
    void deleteBookTest() {
        LoginResponseModel auth = step("Авторизация через API", () -> AuthAPI.login());
        String token = auth.getToken();
        String userId = auth.getUserId();

        step("Очистка коллекции пользователя", () ->
                BooksAPI.deleteAllBooks(token, userId)
        );

        step("Добавление книги через API", () ->
                BooksAPI.addBook(token, userId, BOOKISBN)
        );

        step("Проверка, что книга добавлена через API", () -> {
            UserBooksResponseModel booksResp = BooksAPI.getUserBooks(token, userId);
            List<BookModel> userBooks = booksResp.getBooks();
            assertThat(userBooks).extracting(BookModel::getIsbn).contains(BOOKISBN);
        });

        step("Переход на страницу профиля", () -> {
            open("/profile");
            $("#userName-value").shouldHave(text(USERNAME));
        });

        step("Удаление книги через API", () ->
                BooksAPI.deleteBook(token, userId, BOOKISBN)
        );

        step("Обновление страницы и проверка что книга удалена", () -> {
            Selenide.refresh();
            $(".rt-noData").shouldHave(text("No rows found"));
        });

        step("Проверка что книга удалена через API", () -> {
            UserBooksResponseModel booksResp = BooksAPI.getUserBooks(token, userId);
            assertThat(booksResp.getBooks()).noneMatch(b -> b.getIsbn().equals(BOOKISBN));
        });


    }
}
