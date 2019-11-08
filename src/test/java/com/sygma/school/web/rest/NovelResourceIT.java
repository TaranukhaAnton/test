package com.sygma.school.web.rest;

import com.sygma.school.Jh9App;
import com.sygma.school.domain.Novel;
import com.sygma.school.repository.NovelRepository;
import com.sygma.school.service.NovelService;
import com.sygma.school.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.sygma.school.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link NovelResource} REST controller.
 */
@SpringBootTest(classes = Jh9App.class)
public class NovelResourceIT {

    private static final String DEFAULT_ARTICLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ARTICLE_NAME = "BBBBBBBBBB";

    @Autowired
    private NovelRepository novelRepository;

    @Mock
    private NovelRepository novelRepositoryMock;

    @Mock
    private NovelService novelServiceMock;

    @Autowired
    private NovelService novelService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restNovelMockMvc;

    private Novel novel;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NovelResource novelResource = new NovelResource(novelService);
        this.restNovelMockMvc = MockMvcBuilders.standaloneSetup(novelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Novel createEntity(EntityManager em) {
        Novel novel = new Novel()
            .articleName(DEFAULT_ARTICLE_NAME);
        return novel;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Novel createUpdatedEntity(EntityManager em) {
        Novel novel = new Novel()
            .articleName(UPDATED_ARTICLE_NAME);
        return novel;
    }

    @BeforeEach
    public void initTest() {
        novel = createEntity(em);
    }

    @Test
    @Transactional
    public void createNovel() throws Exception {
        int databaseSizeBeforeCreate = novelRepository.findAll().size();

        // Create the Novel
        restNovelMockMvc.perform(post("/api/novels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(novel)))
            .andExpect(status().isCreated());

        // Validate the Novel in the database
        List<Novel> novelList = novelRepository.findAll();
        assertThat(novelList).hasSize(databaseSizeBeforeCreate + 1);
        Novel testNovel = novelList.get(novelList.size() - 1);
        assertThat(testNovel.getArticleName()).isEqualTo(DEFAULT_ARTICLE_NAME);
    }

    @Test
    @Transactional
    public void createNovelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = novelRepository.findAll().size();

        // Create the Novel with an existing ID
        novel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNovelMockMvc.perform(post("/api/novels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(novel)))
            .andExpect(status().isBadRequest());

        // Validate the Novel in the database
        List<Novel> novelList = novelRepository.findAll();
        assertThat(novelList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllNovels() throws Exception {
        // Initialize the database
        novelRepository.saveAndFlush(novel);

        // Get all the novelList
        restNovelMockMvc.perform(get("/api/novels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(novel.getId().intValue())))
            .andExpect(jsonPath("$.[*].articleName").value(hasItem(DEFAULT_ARTICLE_NAME)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllNovelsWithEagerRelationshipsIsEnabled() throws Exception {
        NovelResource novelResource = new NovelResource(novelServiceMock);
        when(novelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restNovelMockMvc = MockMvcBuilders.standaloneSetup(novelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restNovelMockMvc.perform(get("/api/novels?eagerload=true"))
        .andExpect(status().isOk());

        verify(novelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllNovelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        NovelResource novelResource = new NovelResource(novelServiceMock);
            when(novelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restNovelMockMvc = MockMvcBuilders.standaloneSetup(novelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restNovelMockMvc.perform(get("/api/novels?eagerload=true"))
        .andExpect(status().isOk());

            verify(novelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getNovel() throws Exception {
        // Initialize the database
        novelRepository.saveAndFlush(novel);

        // Get the novel
        restNovelMockMvc.perform(get("/api/novels/{id}", novel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(novel.getId().intValue()))
            .andExpect(jsonPath("$.articleName").value(DEFAULT_ARTICLE_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingNovel() throws Exception {
        // Get the novel
        restNovelMockMvc.perform(get("/api/novels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNovel() throws Exception {
        // Initialize the database
        novelService.save(novel);

        int databaseSizeBeforeUpdate = novelRepository.findAll().size();

        // Update the novel
        Novel updatedNovel = novelRepository.findById(novel.getId()).get();
        // Disconnect from session so that the updates on updatedNovel are not directly saved in db
        em.detach(updatedNovel);
        updatedNovel
            .articleName(UPDATED_ARTICLE_NAME);

        restNovelMockMvc.perform(put("/api/novels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNovel)))
            .andExpect(status().isOk());

        // Validate the Novel in the database
        List<Novel> novelList = novelRepository.findAll();
        assertThat(novelList).hasSize(databaseSizeBeforeUpdate);
        Novel testNovel = novelList.get(novelList.size() - 1);
        assertThat(testNovel.getArticleName()).isEqualTo(UPDATED_ARTICLE_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingNovel() throws Exception {
        int databaseSizeBeforeUpdate = novelRepository.findAll().size();

        // Create the Novel

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNovelMockMvc.perform(put("/api/novels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(novel)))
            .andExpect(status().isBadRequest());

        // Validate the Novel in the database
        List<Novel> novelList = novelRepository.findAll();
        assertThat(novelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNovel() throws Exception {
        // Initialize the database
        novelService.save(novel);

        int databaseSizeBeforeDelete = novelRepository.findAll().size();

        // Delete the novel
        restNovelMockMvc.perform(delete("/api/novels/{id}", novel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Novel> novelList = novelRepository.findAll();
        assertThat(novelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Novel.class);
        Novel novel1 = new Novel();
        novel1.setId(1L);
        Novel novel2 = new Novel();
        novel2.setId(novel1.getId());
        assertThat(novel1).isEqualTo(novel2);
        novel2.setId(2L);
        assertThat(novel1).isNotEqualTo(novel2);
        novel1.setId(null);
        assertThat(novel1).isNotEqualTo(novel2);
    }
}
