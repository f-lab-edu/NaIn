package com.flab.recipebook.recipe.service;

import com.flab.recipebook.recipe.domain.Recipe;
import com.flab.recipebook.recipe.domain.dao.RecipeDao;
import com.flab.recipebook.recipe.dto.ResponseRecipeDto;
import com.flab.recipebook.recipe.dto.SaveRecipeDto;
import com.flab.recipebook.recipe.dto.SearchRecipeDto;
import com.flab.recipebook.recipe.exception.AccessDeniedException;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    @DisplayName("검색 keyword 조회시 변환된 ResponseRecipeDto를 반환한다.")
    void findTitle() {
        User user1 = new User(1L, "User1", "abc1234!@#", "jm@naver.com", UserRole.USER, LocalDateTime.now(), LocalDateTime.now(), null);
        User user2 = new User(2L, "User2", "abc1234!@#", "kim@naver.com", UserRole.USER, LocalDateTime.now(), LocalDateTime.now(), null);

        Recipe recipe1 = new Recipe(1L, "Title1", user1, "type1, type2", "content1 입니다.", LocalDateTime.now().plus(10, ChronoUnit.DAYS), LocalDateTime.now().plus(10, ChronoUnit.DAYS));
        Recipe recipe2 = new Recipe(2L, "Title2", user2, "type1, type2", "content2 입니다.", LocalDateTime.now().plus(12, ChronoUnit.DAYS), LocalDateTime.now().plus(12, ChronoUnit.DAYS));

        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipe1);
        recipeList.add(recipe2);

        SearchRecipeDto searchRecipeDto = new SearchRecipeDto("t", "Title");
        when(recipeDao.findByKeyword(searchRecipeDto)).thenReturn(recipeList);

        assertThat(recipeService.findByKeyword(searchRecipeDto)).extracting(ResponseRecipeDto::getTitle).containsExactly("Title1", "Title2");
        assertThat(recipeService.findByKeyword(searchRecipeDto)).extracting(ResponseRecipeDto::getUserId).containsExactly("User1", "User2");
    }

    //레시피를 조회한다.
    //레시피는 기본적으로 최근 등록순으로 정렬되어서 조회 된다.
    //레시피 메인 검색은 제목에 포함된 문자열을 찾아 조회 된다.
    @Test
    @DisplayName("Recipe 를 전체 조회 한다.")
    void findRecipeAll() {
        User user1 = new User(1L, "User1", "abc1234!@#", "jm@naver.com", UserRole.USER, LocalDateTime.now(), LocalDateTime.now(), null);
        User user2 = new User(2L, "User2", "abc1234!@#", "kim@naver.com", UserRole.USER, LocalDateTime.now(), LocalDateTime.now(), null);

        Recipe recipe1 = new Recipe(1L, "Title1", user1, "type1, type2", "content1 입니다.", LocalDateTime.now().plus(10, ChronoUnit.DAYS), LocalDateTime.now().plus(10, ChronoUnit.DAYS));
        Recipe recipe2 = new Recipe(2L, "Title2", user2, "type1, type2", "content2 입니다.", LocalDateTime.now().plus(12, ChronoUnit.DAYS), LocalDateTime.now().plus(12, ChronoUnit.DAYS));

        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipe1);
        recipeList.add(recipe2);

        when(recipeDao.findAll()).thenReturn(recipeList);

        assertThat(recipeService.findAll()).extracting(ResponseRecipeDto::getRecipeId).containsExactly(1L, 2L);
        assertThat(recipeService.findAll()).extracting(ResponseRecipeDto::getUserId).containsExactly("User1", "User2");
    }

    //레시피를 등록한다
    //레시피 등록은 권한인 CHEF인 회원만 등록할 수 있다.
    //레시피에는 제목, 재료, 내용을 작성할 수 있다.
    @Test
    @DisplayName("Recipe 를 저장한다 - CHEF 계정 성공")
    void saveRecipe() {
        User user = new User(1L, "User1", "abc1234!@#", "jm@naver.com", UserRole.CHEF, LocalDateTime.now(), LocalDateTime.now(), null);
        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        List<String> recipeType = new ArrayList<>();
        recipeType.add("type1");
        recipeType.add("type2");

        SaveRecipeDto saveRecipeDto = new SaveRecipeDto("Title", user.getUserNo(), recipeType ,"content 입니다.");

        recipeService.saveRecipe(saveRecipeDto);
        verify(recipeDao, times(1)).save(any(Recipe.class));
    }

    @Test
    @DisplayName("Recipe 를 저장한다 - CHEF 계정아님 실패")
    void saveRecipeError() {
        User user = new User(1L, "User1", "abc1234!@#", "jm@naver.com", UserRole.USER, LocalDateTime.now(), LocalDateTime.now(), null);
        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        List<String> recipeType = new ArrayList<>();
        recipeType.add("type1");
        recipeType.add("type2");

        SaveRecipeDto saveRecipeDto = new SaveRecipeDto("Title", user.getUserNo(), recipeType, "content 입니다.");

        assertThrows(AccessDeniedException.class, () -> recipeService.saveRecipe(saveRecipeDto));
    }
}