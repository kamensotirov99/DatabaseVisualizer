package dbProject.classes;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name="doctors")
public class Doctors implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="doc_id")
	private int docId;
	
	@ManyToOne
    @JoinColumn(name="dept_id", nullable=false)
	private Departments departments;
	
	@ManyToOne
    @JoinColumn(name="spec_id", nullable=false)
	private Specialties specialties;
	
	@Column(name="doc_fname",length=45,nullable=false)
	private String docFname;
	
	@Column(name="doc_lname",length=45,nullable=false)
	private String docLname;
	
	@Column(name="doc_phonenum",length=45,nullable=false,unique=true)
	private String docPhonenum;
	

	
	
	
	@OneToMany(mappedBy="doctors",fetch=FetchType.LAZY)
	private Set <Treatments>treatmentses = new HashSet<Treatments>();

	public Doctors() {
	}

	public Doctors(int docId, Departments departments, Specialties specialties, String docFname, String docLname,
			String docPhonenum) {
		this.docId = docId;
		this.departments = departments;
		this.specialties = specialties;
		this.docFname = docFname;
		this.docLname = docLname;
		this.docPhonenum = docPhonenum;
	}

	public Doctors(int docId, Departments departments, Specialties specialties, String docFname, String docLname,
			String docPhonenum, Set<Treatments> treatmentses) {
		this.docId = docId;
		this.departments = departments;
		this.specialties = specialties;
		this.docFname = docFname;
		this.docLname = docLname;
		this.docPhonenum = docPhonenum;
		this.treatmentses = treatmentses;
	}

	public int getDocId() {
		return this.docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public Integer getDepartments() {
		return this.departments.getDeptId();
	}

	public void setDepartments(Departments departments) {
		this.departments = departments;
	}

	public Integer getSpecialties() {
		return this.specialties.getSpecId();
	}
	
	public Specialties getSpecs() {
		return this.specialties;
	}
	
	public Departments getDepts() {
		return this.departments;
	}

	public void setSpecialties(Specialties specialties) {
		this.specialties = specialties;
	}

	public String getDocFname() {
		return this.docFname;
	}

	public void setDocFname(String docFname) {
		this.docFname = docFname;
	}

	public String getDocLname() {
		return this.docLname;
	}

	public void setDocLname(String docLname) {
		this.docLname = docLname;
	}

	public String getDocPhonenum() {
		return this.docPhonenum;
	}

	public void setDocPhonenum(String docPhonenum) {
		this.docPhonenum = docPhonenum;
	}

	public Set<Treatments> getTreatmentses() {
		return this.treatmentses;
	}

	public void setTreatmentses(Set<Treatments> treatmentses) {
		this.treatmentses = treatmentses;
	}



	

}
