package com.sygma.school.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Article.
 */
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "article_name")
    private String articleName;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "article_authors",
               joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "authors_id", referencedColumnName = "id"))
    private Set<Author> authors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArticleName() {
        return articleName;
    }

    public Article articleName(String articleName) {
        this.articleName = articleName;
        return this;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public Article authors(Set<Author> authors) {
        this.authors = authors;
        return this;
    }

    public Article addAuthors(Author author) {
        this.authors.add(author);
        author.getArticles().add(this);
        return this;
    }

    public Article removeAuthors(Author author) {
        this.authors.remove(author);
        author.getArticles().remove(this);
        return this;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        return id != null && id.equals(((Article) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", articleName='" + getArticleName() + "'" +
            "}";
    }
}
