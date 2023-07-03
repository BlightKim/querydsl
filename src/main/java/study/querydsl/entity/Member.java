package study.querydsl.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@NamedQuery(
    name = "Member.findByUsername",
    query = "select m from Member m where m.username = :username"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "member_id")
  private Long id;

  private String username;

  private int age;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  public Member(String username, int age, Team team) {
    this.username = username;
    this.age = age;
    if (team != null) {
      changeTeam(team);
    }
  }

  public Member(String username) {
    this.username = username;
  }

  public Member(String username, int age) {
    this.username = username;
    this.age = age;
  }

  public void changeTeam(Team team) {
    this.team = team;
    team.getMembers().add(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Member member = (Member) o;
    return getAge() == member.getAge() && Objects.equals(getId(), member.getId())
        && Objects.equals(getUsername(), member.getUsername()) && Objects.equals(
        getTeam(), member.getTeam());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getUsername(), getAge(), getTeam());
  }
}
