package dbProject.classes;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="specialties")
public class Specialties implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="spec_id")
	private int specId;
	
	@Column(name="spec_desc",length=45,nullable=false,unique=true)
	private String specDesc;
	
	@OneToMany(mappedBy="specialties",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private Set<Doctors> doctorses = new HashSet <Doctors>();

	public Specialties() {
	}

	public Specialties( String specDesc) {
		
		this.specDesc = specDesc;
	}

	public Specialties(int specId, String specDesc, Set<Doctors> doctorses) {
		this.specId = specId;
		this.specDesc = specDesc;
		this.doctorses = doctorses;
	}

	public int getSpecId() {
		return this.specId;
	}

	public void setSpecId(int specId) {
		this.specId = specId;
	}

	public String getSpecDesc() {
		return this.specDesc;
	}

	public void setSpecDesc(String specDesc) {
		this.specDesc = specDesc;
	}

	public Set<Doctors> getDoctorses() {
		return this.doctorses;
	}

	public void setDoctorses(Set<Doctors> doctorses) {
		this.doctorses = doctorses;
	}

}
