package eu.fbk.PapyGame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PostgreSqlService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public String getContentByProjectId(String project_id) {
        String sql = "SELECT content FROM document WHERE project_id = \'" + project_id + "\'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public String getDocumentIdByProjectId(String project_id) {
        String sql = "SELECT id FROM document WHERE project_id = \'" + project_id + "\'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }
}
