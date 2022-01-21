package cn.chenshaotong.entity;

import java.io.Serializable;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", schema = "web-self", catalog = "")
public class User implements Serializable {
  @GenericGenerator(name = "uuid", strategy = "uuid")
  @GeneratedValue(generator = "uuid")
  @Id
  @Column(name = "id", nullable = false, length = 32)
  public String id;

  @Basic
  @Column(name = "name", nullable = false, length = 1000)
  public String name;

  @Basic
  @Column(name = "password", nullable = true, length = 1000)
  public String password;

  @Basic
  @Column(name = "belong", nullable = true, length = 1000)
  public String belong;
}
