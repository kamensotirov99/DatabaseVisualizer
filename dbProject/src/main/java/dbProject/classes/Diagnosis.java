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
@Table(name="diagnosis")
public class Diagnosis implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="diag_id")
	private int diagId;
	
	@Column(name="diag_desc",length=45,nullable=false,unique=true)
	private String diagDesc;
	
	@OneToMany(mappedBy="diagnosis",fetch=FetchType.LAZY)
	private Set<Treatments> treatmentses = new HashSet<Treatments>();

	public Diagnosis() {
	}

	public Diagnosis(int diagId, String diagDesc) {
		this.diagId = diagId;
		this.diagDesc = diagDesc;
	}

	public Diagnosis(int diagId, String diagDesc, Set<Treatments> treatmentses) {
		this.diagId = diagId;
		this.diagDesc = diagDesc;
		this.treatmentses = treatmentses;
	}

	public int getDiagId() {
		return this.diagId;
	}

	public void setDiagId(int diagId) {
		this.diagId = diagId;
	}

	public String getDiagDesc() {
		return this.diagDesc;
	}

	public void setDiagDesc(String diagDesc) {
		this.diagDesc = diagDesc;
	}

	public Set<Treatments> getTreatmentses() {
		return this.treatmentses;
	}

	public void setTreatmentses(Set<Treatments> treatmentses) {
		this.treatmentses = treatmentses;
	}

}
