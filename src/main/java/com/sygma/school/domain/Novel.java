package com.sygma.school.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Novel.
 */
@Entity
@Table(name = "novel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Novel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "article_name")
    private String articleName;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "novel_authors",
               joinColumns = @JoinColumn(name = "novel_id", referencedColumnName = "id"),
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

    public Novel articleName(String articleName) {
        this.articleName = articleName;
        return this;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public Novel authors(Set<Author> authors) {
        this.authors = authors;
        return this;
    }

    public Novel addAuthors(Author author) {
        this.authors.add(author);
        author.getNovels().add(this);
        return this;
    }

    public Novel removeAuthors(Author author) {
        this.authors.remove(author);
        author.getNovels().remove(this);
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
        if (!(o instanceof Novel)) {
            return false;
        }
        return id != null && id.equals(((Novel) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Novel{" +
            "id=" + getId() +
            ", articleName='" + getArticleName() + "'" +
            "}";
    }
}
