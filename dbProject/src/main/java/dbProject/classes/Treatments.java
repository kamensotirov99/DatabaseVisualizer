package dbProject.classes;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="treatments")
public class Treatments implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="tr_id")
	private int trId;
	
	@ManyToOne
    @JoinColumn(name="diag_id", nullable=false)
	private Diagnosis diagnosis;
	
	@ManyToOne
    @JoinColumn(name="doc_id", nullable=false)
	private Doctors doctors;
	
	@ManyToOne
    @JoinColumn(name="pat_id", nullable=false)
	private Patients patients;
	
	@Column(name="st_date",nullable=false)
	private LocalDate stDate;
	
	@Column(name="end_date",nullable=false)
	private LocalDate endDate;
	
	@Column(name="amount_due",nullable=false)
	private int amountDue;

	public Treatments() {
	}

	public Treatments(int trId, Diagnosis diagnosis, Doctors doctors, Patients patients, LocalDate stDate, LocalDate endDate,
			int amountDue) {
		this.trId = trId;
		this.diagnosis = diagnosis;
		this.doctors = doctors;
		this.patients = patients;
		this.stDate = stDate;
		this.endDate = endDate;
		this.amountDue = amountDue;
	}

	public int getTrId() {
		return this.trId;
	}

	public void setTrId(int trId) {
		this.trId = trId;
	}

	public Integer getDiagnosis() {
		return this.diagnosis.getDiagId();
	}
	
	public Diagnosis getDiag() {
		return this.diagnosis;
	}

	public void setDiagnosis(Diagnosis diagnosis) {
		this.diagnosis = diagnosis;
	}

	public int getDoctors() {
		return this.doctors.getDocId();
	}

	public Doctors getDoctor() {
		return this.doctors;
	}
	public void setDoctors(Doctors doctors) {
		this.doctors = doctors;
	}

	public int getPatients() {
		return this.patients.getPatId();
	}
	
	public Patients getPatient() {
		return this.patients;
	}

	public void setPatients(Patients patients) {
		this.patients = patients;
	}

	public LocalDate getStDate() {
		return this.stDate;
	}

	public void setStDate(LocalDate stDate) {
		this.stDate = stDate;
	}

	public LocalDate getEndDate() {
		return this.endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getAmountDue() {
		return this.amountDue;
	}

	public void setAmountDue(int amountDue) {
		this.amountDue = amountDue;
	}

}
