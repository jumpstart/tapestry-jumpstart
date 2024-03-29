package jumpstart.business.domain.workout;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.DoesNotExistException;
import jumpstart.business.domain.base.BaseService;
import jumpstart.business.domain.workout.iface.IWorkoutServiceLocal;
import jumpstart.business.domain.workout.iface.IWorkoutServiceRemote;

@Stateless
@Local(IWorkoutServiceLocal.class)
@Remote(IWorkoutServiceRemote.class)
public class WorkoutService extends BaseService implements IWorkoutServiceLocal, IWorkoutServiceRemote {

	// Building

	public Building createBuilding(Building building) throws BusinessException {
		persist(building);
		return building;
	}

	public void changeBuilding(Building building) throws BusinessException {
		Building b = merge(building);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!b.getId().equals(building.getId())) {
			throw new DoesNotExistException(Building.class, building.getId());
		}

		flushToWorkAroundTOMEE_172();
	}

	public void deleteBuilding(Building building) throws BusinessException {
		Building b = merge(building);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!b.getId().equals(building.getId())) {
			throw new DoesNotExistException(Building.class, building.getId());
		}

		remove(b);
		flushToWorkAroundTOMEE_172();
	}

	public void deleteAllBuildings() throws BusinessException {
		em.createQuery("delete from Building").executeUpdate();
	}

	public Building findBuilding(Long id) throws DoesNotExistException {
		Building obj = findStrict(Building.class, id);
		return obj;
	}

	public Building findBuildingAndItsRooms(Long id) throws DoesNotExistException {

		try {
			StringBuilder buf = new StringBuilder();
			buf.append("select distinct b from Building b");
			buf.append(" left join fetch b.rooms");
			buf.append(" where b.id = :id");

			Query q = em.createQuery(buf.toString());
			q.setParameter("id", id);

			Building obj = (Building) q.getSingleResult();
			return obj;
		}
		catch (NoResultException e) {
			throw new DoesNotExistException(Building.class, id);
		}
		catch (NonUniqueResultException e) {
			throw new IllegalStateException("Duplicate Building found with id = " + id + ".", e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Building> findBuildings() {
		return em.createQuery("select b from Building b order by b.id").getResultList();
	}

	// Room

	public Room addRoom(Room room, boolean checkBuildingUnchanged) throws BusinessException {

		if (checkBuildingUnchanged) {
			merge(room.getBuilding());
		}

		persist(room);
		return room;
	}

	public void changeRoom(Room room) throws BusinessException {
		Room r = merge(room);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!r.getId().equals(room.getId())) {
			throw new DoesNotExistException(Room.class, room.getId());
		}

		flushToWorkAroundTOMEE_172();
	}

	public void removeRoom(Room room) throws BusinessException {
		Room r = merge(room);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!r.getId().equals(room.getId())) {
			throw new DoesNotExistException(Room.class, room.getId());
		}

		remove(r);
		flushToWorkAroundTOMEE_172();
	}

	public void removeAllRooms() throws BusinessException {
		em.createQuery("delete from Room").executeUpdate();
	}

	public Room findRoom(Long id) throws DoesNotExistException {
		Room obj = findStrict(Room.class, id);
		return obj;
	}

	@SuppressWarnings("unchecked")
	public List<Room> findRooms() {
		return em.createQuery("select r from Room r order by r.id").getResultList();
	}

	// Department

	public Department createDepartment(Department department) throws BusinessException {
		persist(department);
		flushToWorkAroundTOMEE_172();
		return department;
	}

	public void changeDepartment(Department department) throws BusinessException {
		Department d = merge(department);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!d.getId().equals(department.getId())) {
			throw new DoesNotExistException(Department.class, department.getId());
		}
	}

	public void deleteDepartment(Department department) throws BusinessException {
		Department d = merge(department);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!d.getId().equals(department.getId())) {
			throw new DoesNotExistException(Department.class, department.getId());
		}

		remove(d);
	}

	public void deleteAllDepartments() throws BusinessException {
		em.createQuery("delete from Department").executeUpdate();
	}

	public Department findDepartment(Long id) throws DoesNotExistException {
		Department obj = findStrict(Department.class, id);
		return obj;
	}

	public Department findDepartmentAndItsTeachers(Long id) throws DoesNotExistException {

		StringBuilder buf = new StringBuilder();
		buf.append("select distinct d from Department d");
		buf.append(" left join fetch d.teachers");
		buf.append(" where d.id = :id");

		Query q = em.createQuery(buf.toString());
		q.setParameter("id", id);

		try {
			Department obj = (Department) q.getSingleResult();
			return obj;
		}
		catch (NoResultException e) {
			throw new DoesNotExistException(Department.class, id);
		}
		catch (NonUniqueResultException e) {
			throw new IllegalStateException("Duplicate Department found with id = " + id + ".", e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Department> findDepartments() {
		return em.createQuery("select c from Department c order by c.id").getResultList();
	}

	// Teacher

	public Teacher createTeacher(Teacher teacher, boolean checkDepartmentUnchanged) throws BusinessException {

		if (checkDepartmentUnchanged) {
			merge(teacher.getDepartment());
		}
		persist(teacher);
		return teacher;
	}

	public void changeTeacher(Teacher teacher) throws BusinessException {
		Teacher t = merge(teacher);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!t.getId().equals(teacher.getId())) {
			throw new DoesNotExistException(Teacher.class, teacher.getId());
		}

		flushToWorkAroundTOMEE_172();
	}

	public void deleteTeacher(Teacher teacher) throws BusinessException {
		Teacher t = merge(teacher);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!t.getId().equals(teacher.getId())) {
			throw new DoesNotExistException(Teacher.class, teacher.getId());
		}

		remove(t);
	}

	public void deleteAllTeachers() throws BusinessException {
		em.createQuery("delete from Teacher").executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<Teacher> findTeachers() {
		return em.createQuery("select t from Teacher t order by t.id").getResultList();
	}

	public Teacher findTeacher(Long id) throws DoesNotExistException {
		Teacher obj = findStrict(Teacher.class, id);
		return obj;
	}

	public Teacher findTeacherShallowish(Long id) throws DoesNotExistException {
		try {
			// "Shallowish" means join fetch the immediate ManyToOne references.
			StringBuilder buf = new StringBuilder();
			buf.append("select t from Teacher t");
			buf.append(" left join fetch t.department");
			buf.append(" left join fetch t.favouriteRoom");
			buf.append(" where t.id = :id");

			Query q = em.createQuery(buf.toString());
			q.setParameter("id", id);

			Teacher obj = (Teacher) q.getSingleResult();
			return obj;
		}
		catch (NoResultException e) {
			throw new DoesNotExistException(Teacher.class, id);
		}
		catch (NonUniqueResultException e) {
			throw new IllegalStateException("Duplicate Teacher found with id = " + id + ".", e);
		}
	}

	// Student

	public Student createStudent(Student student, boolean checkTeacherUnchanged) throws BusinessException {

		if (checkTeacherUnchanged) {
			merge(student.getTeacher());
		}
		persist(student);
		return student;
	}

	public void changeStudent(Student student) throws BusinessException {
		Student s = merge(student);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!s.getId().equals(student.getId())) {
			throw new DoesNotExistException(Student.class, student.getId());
		}
	}

	public void deleteStudent(Student student) throws BusinessException {
		Student s = merge(student);
		
		// If id is different it means the person did not exist so merge has created a new one.
		if (!s.getId().equals(student.getId())) {
			throw new DoesNotExistException(Student.class, student.getId());
		}

		remove(s);
	}

	public void deleteAllStudents() throws BusinessException {
		em.createQuery("delete from Student").executeUpdate();
	}

	public Student findStudent(Long id) throws DoesNotExistException {
		Student obj = findStrict(Student.class, id);
		return obj;
	}

	// StringThing

	public StringThing createStringThing(StringThing stringThing) throws BusinessException {
		persist(stringThing);
		flushToWorkAroundTOMEE_172();
		return stringThing;
	}

	public void deleteAllStringThings() throws BusinessException {
		em.createQuery("delete from StringThing").executeUpdate();
	}

	public StringThing findStringThing(String id) throws DoesNotExistException {
		StringThing obj = findStrict(StringThing.class, id);
		return obj;
	}

}
