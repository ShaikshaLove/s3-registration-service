package io.s3soft.registration.model;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author shaiksha
 *
 */
@Entity
@Table(name="user_tab")
public class User implements Serializable,Comparable<User>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 73L;
	@Id
	@GenericGenerator(name="id-gen",strategy = "io.s3soft.registration.generator.UserIdGernerator")
	@GeneratedValue(generator = "id-gen")
	private long userId;
	@Column(name = "enabled")
    private boolean enabled;
	@Column(name="firstName")
	private String firstName;
	@Column(name="lastName")
	private String lastName;
	@Column(name="email")
	private String email;
	@Column(name="password")
	private String password;
	@ElementCollection(fetch=FetchType.EAGER)
	@JoinColumn(name="user_id_fk")
	private List<String> roles;
	@Temporal(TemporalType.TIMESTAMP)
	private Date accountCreated;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	
	@OneToOne(mappedBy="user",cascade=CascadeType.ALL)
	@JsonIgnore
	private VerificationToken verificationToken;


	public VerificationToken getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(VerificationToken verificationToken) {
		this.verificationToken = verificationToken;
	}

	public Date getAccountCreated() {
		return accountCreated;
	}

	public void setAccountCreated(Date accountCreated) {
		this.accountCreated = accountCreated;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}




	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int compareTo(User o) {
		return (int) (this.getUserId()-o.getUserId());
	}

	public User() {
		super();
		this.enabled=false;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", enabled=" + enabled + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", password=" + password + ", roles=" + roles + ", accountCreated="
				+ accountCreated + ", modifiedDate=" + modifiedDate + ", verificationToken=" + verificationToken + "]";
	}	
}
