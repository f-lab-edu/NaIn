package com.flab.recipebook.recipe.domain.dao;

import com.flab.recipebook.recipe.domain.Recipe;
import com.flab.recipebook.user.domain.User;
import com.flab.recipebook.user.domain.UserRole;
import com.flab.recipebook.user.domain.dao.UserDao;
import com.flab.recipebook.user.dto.SaveUserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
class RecipeDaoTest {

    @Autowired
    UserDao userDao;

    @Autowired
    RecipeDao recipeDao;

    @Test
    @DisplayName("Recipe 저장 후 recipeId를 사용해 recipe을 조회 한다.")
    void findById() {
        SaveUserDto saveUserDto = new SaveUserDto("test", "abc1234!@#", "jm@naver.com");
        User saveUser = new User(saveUserDto.getUserId(), saveUserDto.getPassword(), saveUserDto.getEmail(), UserRole.USER);

        userDao.save(saveUser);
        User user = userDao.findByUserId("test").orElse(null);

        Recipe recipe = new Recipe(1L, "RecipeTitle", user.getUserNo(), "RecipeContent 입니다.", LocalDateTime.now(), LocalDateTime.now());
        recipeDao.save(recipe);

        Recipe findRecipe = recipeDao.findById(1L).get();

        assertThat(findRecipe.getRecipeId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("recipe title을 검색하여 recipe를 가져온다")
    void findByTitle() {
        User user1 = new User(1L, "User1", "abc1234!@#", "jm.naver.com", UserRole.USER, LocalDateTime.now(), LocalDateTime.now(), null);
        User user2 = new User(2L, "User2", "abc1234!@#", "kim.naver.com", UserRole.USER, LocalDateTime.now(), LocalDateTime.now(), null);

        userDao.save(user1);
        userDao.save(user2);

        Recipe recipe1 = new Recipe(1L, "Title1", user1.getUserNo(), "content1 입니다.", LocalDateTime.now(), LocalDateTime.now());
        Recipe recipe2 = new Recipe(2L, "Title2", user2.getUserNo(), "content2 입니다.", LocalDateTime.now(), LocalDateTime.now());

        recipeDao.save(recipe1);
        recipeDao.save(recipe2);

        String title = "Title";
        List<Recipe> recipeList = recipeDao.findByTitle(title);

        assertThat(recipeList).extracting(Recipe::getTitle).containsExactly("Title1", "Title2");
    }
}