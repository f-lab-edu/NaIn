package com.flab.recipebook.recipe.domain.dao;

import com.flab.recipebook.recipe.domain.Recipe;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface RecipeDao {
    void save(Recipe recipe);

    Optional<Recipe> findById(Long id);

    List<Recipe> findByTitle(String title);
}
