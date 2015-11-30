package hu.bme.aut.kutatasinaplo.view.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement(name = "users")
@Getter
@Setter
public class UsersVO {

	private List<UserVO> users = Lists.newArrayList();

}
