package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity(name = "users")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email", "username"}))
public class User implements Serializable {
  /**
   * Default value included to remove warning. Remove or modify at will. *
   */
  private static final long serialVersionUID = 1L;

  public User() {
  }

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.tasks = new ArrayList<>();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String username;

  @NotNull
  private String email;

  @NotNull
  private String password;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private Collection<Task> tasks;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
  private Collection<Group> owner_groups;

  ///@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "members")
  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "members")
  private Collection<Group> member_groups = new ArrayList<>();

  @JsonView(Views.Private.class)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @JsonView(Views.Private.class)
  public String getEmail() {
    return email;
  }

  @JsonView(Views.Public.class)
  public String getUsername() {
    return username;
  }

  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @JsonView(Views.Complete.class)
  public Collection<Task> getTasks() {
    // Since tasks is collection controlled by JPA, it has LAZY loading by default. That means
    // that you have to query the object (calling size(), for example) to get the list initialized
    // More: http://www.javabeat.net/jpa-lazy-eager-loading/
    tasks.size();
    return tasks;
  }

  public void addTask(Task task) {
    tasks.add(task);
  }

  @JsonIgnore
  public Collection<Group> getOwner_groups() {
      return owner_groups;
  }

  public void addOwner_group(Group owner_group) {
      this.owner_groups.add(owner_group);
  }

  @JsonIgnore
  public Collection<Group> getMember_groups() {
      return member_groups;
  }

  public void addMember_groups(Group member_group) {
      this.member_groups.add(member_group);
  }
}
