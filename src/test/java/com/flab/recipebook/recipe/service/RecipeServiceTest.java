package com.flab.recipebook.recipe.service;

import com.flab.recipebook.recipe.domain.Recipe;
import com.flab.recipebook.recipe.domain.dao.RecipeDao;
import com.flab.recipebook.recipe.dto.ResponseRecipeDto;
import com.flab.recipebook.recipe.exception.RecipeNotFoundException;
import com.flab.recipebook.user.domain.User;
import com.flab.recipebook.user.domain.UserRole;
import com.flab.recipebook.user.domain.dao.UserDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    UserDao userDao;

    @Mock
    RecipeDao recipeDao;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    @DisplayName("recipeId로 조회시 Recipe가 없으면 RecipeNotFoundException 을 발생시킨다")
    void findByIdError() {
        when(recipeDao.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RecipeNotFoundException.class, () -> recipeService.findById(2L));
    }

    @Test
    @DisplayName("title 조회시 변환된 ResponseRecipeDto를 반환한다.")
    void findTitle() {
        User user1 = new User(1L, "User1", "abc1234!@#", "jm.naver.com", UserRole.USER, LocalDateTime.now(), LocalDateTime.now(), null);
        User user2 = new User(2L, "User2", "abc1234!@#", "kim.naver.com", UserRole.USER, LocalDateTime.now(), LocalDateTime.now(), null);

        Recipe recipe1 = new Recipe(1L, "Title1", 1L, "content1 입니다.", LocalDateTime.now(), LocalDateTime.now());
        Recipe recipe2 = new Recipe(2L, "Title2", 2L, "content2 입니다.", LocalDateTime.now(), LocalDateTime.now());

        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipe1);
        recipeList.add(recipe2);

        String title = "Title";
        when(recipeDao.findByTitle(title)).thenReturn(recipeList);
        when(userDao.findById(1L)).thenReturn(user1);
        when(userDao.findById(2L)).thenReturn(user2);

        assertThat(recipeService.findByTitle(title)).extracting(ResponseRecipeDto::getTitle).containsExactly("Title1", "Title2");
        assertThat(recipeService.findByTitle(title)).extracting(ResponseRecipeDto::getUserId).containsExactly("User1", "User2");
    }
    //레시피를 등록한다
    //레시피 등록은 권한인 CHEF인 회원만 등록할 수 있다.
    //레시피에는 제목, 재료, 내용을 작성할 수 있다.

    //레시피를 조회한다.
    //레시피는 기본적으로 최근 등록순으로 정렬되어서 조회 된다.
    //레시피 메인 검색은 제목에 포함된 문자열을 찾아 조회 된다.

    //레시피 상세
    //검색된 레시피를 클릭하면 상세한 내용을 볼 수 있다.
    //레시피 상세 조회를 하면 댓글을 남길 수 있다.
    //레시피 상세 조회를 하면 별점을 남길 수 있다.

}