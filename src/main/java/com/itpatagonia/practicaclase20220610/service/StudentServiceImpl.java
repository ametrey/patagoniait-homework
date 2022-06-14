package com.itpatagonia.practicaclase20220610.service;

import com.itpatagonia.practicaclase20220610.exception.NoEntityException;
import com.itpatagonia.practicaclase20220610.model.Student;
import com.itpatagonia.practicaclase20220610.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) throws NoEntityException {
        return studentRepository.findById(id).orElseThrow(() -> new NoEntityException("No existe Student con " + id));
    }

    @Transactional
    @Override
    public Student updateStudent(Student student) throws NoEntityException {
        Student studentOld = studentRepository.findById(student.getId()).orElseThrow(
                () -> new NoEntityException("No existe Student con " + student.getId()));
        studentOld.setSurname(student.getSurname());
        studentOld.setName(student.getName());
        studentOld.setBirthday(student.getBirthday());
        return studentRepository.save(studentOld);
    }

    @Transactional
    @Override
    public void deleteStudent(Long id) throws NoEntityException {
        Student student = studentRepository.findById(id).orElseThrow(() -> new NoEntityException("No existe Student con " + id));
        studentRepository.delete(student);
    }

    public int calcularEdad(Long id) throws NoEntityException {
        Optional<Student> student = this.studentRepository.findById(id);
        return Period.between(student.orElseThrow(() -> new NoEntityException("No existe Student con " + id)).getBirthday(),
                LocalDate.now()).getYears();
    }

    //Completar
    public int getAverageAge() {
        return (int) studentRepository.findAll().stream().mapToInt(student -> Period.between(student.getBirthday(), LocalDate.now()).getYears()).average().orElseThrow();
    }

    public int sumAllAges() {
        return (int)studentRepository.findAll().stream().mapToInt(student -> Period.between(student.getBirthday(), LocalDate.now()).getYears()).sum();
    }

	public int getOlderAge() {
		return studentRepository.findAll().stream().mapToInt(student -> Period.between(student.getBirthday(), LocalDate.now()).getYears()).max().orElse(0);
	}

    public int getMinAge() {
        return studentRepository.findAll().stream().mapToInt(student -> Period.between(student.getBirthday(), LocalDate.now()).getYears()).min().orElse(0);
    }

    public String getName() {
        return studentRepository.findAll().stream().map(Student::getName).collect(Collectors.joining(" - "));
    }

    public List<Student> getEstudentsOlderAndC() {
        return studentRepository.findAll().stream().filter(stud -> stud.getName().startsWith("C") && 
        Period.between(stud.getBirthday(), LocalDate.now()).getYears() >= 18)
        .collect(Collectors.toList());
    }

    public List<Student> getEstudentsYoungerAndS() {
        return studentRepository.findAll().stream().filter(stud -> stud.getName().startsWith("S") && 
        Period.between(stud.getBirthday(), LocalDate.now()).getYears() <= 18)
        .collect(Collectors.toList());
    }

    
    public List<Student> getThreeOlderStudents() {
        return studentRepository.findAll().stream().sorted((s1,s2) -> s1.getBirthday().compareTo(s2.getBirthday())).collect(Collectors.toList().)
    }

    //Inventar un ejemplo

}
