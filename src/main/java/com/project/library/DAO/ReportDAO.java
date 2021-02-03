package com.project.library.DAO;

import com.project.library.Model.Report;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDAO extends CrudRepository<Report, Integer> {
}
