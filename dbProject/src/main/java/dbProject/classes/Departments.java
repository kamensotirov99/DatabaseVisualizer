package dbProject.classes;


import java.util.HashSet;
import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name="departments")
public class Departments implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="dept_id")
	private int deptId;
	
	@Column(name="dept_desc",length=45,nullable=false,unique=true)
	private String deptDesc;
	
	@OneToMany(mappedBy="departments",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private Set <Doctors> doctorses = new HashSet<Doctors>();

	public Departments() {
	}

	public Departments(int deptId, String deptDesc) {
		this.deptId = deptId;
		this.deptDesc = deptDesc;
	}

	public Departments(int deptId, String deptDesc, Set<Doctors> doctorses) {
		this.deptId = deptId;
		this.deptDesc = deptDesc;
		this.doctorses = doctorses;
	}

	public int getDeptId() {
		return this.deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getDeptDesc() {
		return this.deptDesc;
	}

	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
	}

	public Set<Doctors> getDoctorses() {
		return this.doctorses;
	}

	public void setDoctorses(Set<Doctors> doctorses) {
		this.doctorses = doctorses;
	}
	
	public void addDoctor(Doctors doc) {
		this.doctorses.add(doc);
	}

}
