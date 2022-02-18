package dbProject.classes;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="patients")
public class Patients implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="pat_id")
	private int patId;
	
	@Column(name="pat_fname",length=45,nullable=false)
	private String patFname;
	
	@Column(name="pat_lname",length=45,nullable=false)
	private String patLname;
	
	@Column(name="pat_address",length=45,nullable=false)
	private String patAddress;
	
	@OneToMany(mappedBy="patients",fetch=FetchType.LAZY)
	private Set<Treatments> treatmentses = new HashSet<Treatments>();

	public Patients() {
	}

	public Patients(int patId, String patFname, String patLname, String patAddress) {
		this.patId = patId;
		this.patFname = patFname;
		this.patLname = patLname;
		this.patAddress = patAddress;
	}

	public Patients(int patId, String patFname, String patLname, String patAddress, Set<Treatments> treatmentses) {
		this.patId = patId;
		this.patFname = patFname;
		this.patLname = patLname;
		this.patAddress = patAddress;
		this.treatmentses = treatmentses;
	}

	public int getPatId() {
		return this.patId;
	}

	public void setPatId(int patId) {
		this.patId = patId;
	}

	public String getPatFname() {
		return this.patFname;
	}

	public void setPatFname(String patFname) {
		this.patFname = patFname;
	}

	public String getPatLname() {
		return this.patLname;
	}

	public void setPatLname(String patLname) {
		this.patLname = patLname;
	}

	public String getPatAddress() {
		return this.patAddress;
	}

	public void setPatAddress(String patAddress) {
		this.patAddress = patAddress;
	}

	public Set<Treatments> getTreatmentses() {
		return this.treatmentses;
	}

	public void setTreatmentses(Set<Treatments> treatmentses) {
		this.treatmentses = treatmentses;
	}

}
