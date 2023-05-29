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
import com.flab.recipebook.user.exception.NotFoundUserException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final UserDao userDao;
    private final RecipeDao recipeDao;

    public RecipeService(UserDao userDao, RecipeDao recipeDao) {
        this.userDao = userDao;
        this.recipeDao = recipeDao;
    }

    /**
     * 레시피를 등록한다
     * 레시피 등록은 권한인 CHEF인 회원만 등록할 수 있다. (security 설정 추가)
     * 레시피에는 제목, 재료, 내용을 작성할 수 있다.
     */
    public void saveRecipe(SaveRecipeDto saveRecipeDto) {
        //권한 검증 && recipe 변환
        Recipe recipe = makeRecipeFromSaveRecipeDto(saveRecipeDto);
        //recipe 저장
        recipeDao.save(recipe);
    }

    /**
     * 레시피를 조회한다.
     * 레시피는 기본적으로 최근 등록순으로 정렬되어서 조회 된다.
     * 레시피 메인 검색은 제목에 포함된 문자열을 찾아 조회 된다.
     */
    public List<ResponseRecipeDto> findByKeyword(SearchRecipeDto searchRecipeDto) {
        //작성자 일 때 아이디 찾기
        return recipeDao.findByKeyword(searchRecipeDto).stream().map(
                recipe -> convertResponseRecipe(recipe)).collect(Collectors.toList());
    }

    /**
     * 레시피를 전체 조회한다.
     * 모든 사용자가 이용 가능하다.
     */
    public List<ResponseRecipeDto> findAll() {
        return recipeDao.findAll().stream().map(
                recipe -> convertResponseRecipe(recipe)).collect(Collectors.toList());
    }

    /**
     * 레시피 상세
     * 검색된 레시피를 클릭하면 상세한 내용을 볼 수 있다.
     * 레시피 상세 조회를 하면 댓글을 남길 수 있다. (댓글기능 추가 후)
     * 레시피 상세 조회를 하면 별점을 남길 수 있다. (별점기능 추가 후)
     */
    public ResponseRecipeDto findById(Long id) {
        Recipe recipe = recipeDao.findById(id).orElseThrow(() -> new RecipeNotFoundException("레시피를 찾을 수 없습니다"));
        return convertResponseRecipe(recipe);
    }

    public Recipe makeRecipeFromSaveRecipeDto(SaveRecipeDto saveRecipeDto) {
        User user = checkUserRole(saveRecipeDto.getUserNo());
        return new Recipe(
                saveRecipeDto.getTitle(),
                user,
                saveRecipeDto.getRecipeType(),
                saveRecipeDto.getContent());
    }

    private User checkUserRole(Long userNo) {
        User user = userDao.findById(userNo).orElseThrow(() -> new NotFoundUserException("유저를 찾을 수 없습니다."));
        if (user == null || !user.getUserRole().equals(UserRole.CHEF)) {
            throw new AccessDeniedException("권한이 없는 계정입니다.");
        }
        return user;
    }

    public ResponseRecipeDto convertResponseRecipe(Recipe recipe) {
        return new ResponseRecipeDto(
                recipe.getRecipeId(),
                recipe.getTitle(),
                recipe.getRecipeType(),
                recipe.getContent(),
                recipe.getCreateDate(),
                recipe.getModifyDate(),
                recipe.getUser().getUserId()
        );
    }
}
