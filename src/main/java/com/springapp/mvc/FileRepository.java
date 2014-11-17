package com.springapp.mvc;

import com.springapp.mvc.classes.dbFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<dbFile, Long>{
}
