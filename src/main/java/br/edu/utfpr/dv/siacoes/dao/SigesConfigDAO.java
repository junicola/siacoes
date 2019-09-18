package br.edu.utfpr.dv.siacoes.dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.edu.utfpr.dv.siacoes.model.SigesConfig;
import br.edu.utfpr.dv.siacoes.model.SigetConfig.SupervisorFilter;

public class SigesConfigDAO extends TemplateDAO<SigesConfig> {

	public SigesConfig findByDepartment(int idDepartment) throws SQLException {
		return findById(idDepartment);
	}

	public int save(int idUser, SigesConfig config) throws SQLException {
		save(idUser, config, config.getDepartment().getIdDepartment());

		return config.getDepartment().getIdDepartment();
	}

	@Override
	protected String getInsertSQL() {
		return "INSERT INTO sigesconfig(minimumScore, supervisorPonderosity, companySupervisorPonderosity, showgradestostudent, supervisorfilter, supervisorFillJuryForm, maxfilesize, jurytime, idDepartment) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	}


	@Override
	protected void setStatement(PreparedStatement stmt, SigesConfig config) throws SQLException {
		stmt.setDouble(1, config.getMinimumScore());
		stmt.setDouble(2, config.getSupervisorPonderosity());
		stmt.setDouble(3, config.getCompanySupervisorPonderosity());
		stmt.setInt(4, config.isShowGradesToStudent() ? 1 : 0);
		stmt.setInt(5, config.getSupervisorFilter().getValue());
		stmt.setInt(6, config.isSupervisorFillJuryForm() ? 1 : 0);
		stmt.setInt(7, config.getMaxFileSize());
		stmt.setInt(8, config.getJuryTime());
		stmt.setInt(9, config.getDepartment().getIdDepartment());
	}

	@Override
	protected String getSelectSQL() {
		return "SELECT * FROM sigesconfig";
	}


	@Override
	protected SigesConfig loadObject(ResultSet rs) throws SQLException {
		SigesConfig config = new SigesConfig();

		config.getDepartment().setIdDepartment(rs.getInt("idDepartment"));
		config.setMinimumScore(rs.getDouble("minimumScore"));
		config.setSupervisorPonderosity(rs.getDouble("supervisorPonderosity"));
		config.setCompanySupervisorPonderosity(rs.getDouble("companySupervisorPonderosity"));
		config.setShowGradesToStudent(rs.getInt("showgradestostudent") == 1);
		config.setSupervisorFilter(SupervisorFilter.valueOf(rs.getInt("supervisorfilter")));
		config.setSupervisorFillJuryForm(rs.getInt("supervisorFillJuryForm") == 1);
		config.setMaxFileSize(rs.getInt("maxfilesize"));
		config.setJuryTime(rs.getInt("jurytime"));

		return config;
	}


	@Override
	protected String getUpdateSQL() {
		return "UPDATE sigesconfig SET minimumScore=?, supervisorPonderosity=?, companySupervisorPonderosity=?, showgradestostudent=?, supervisorfilter=?, supervisorFillJuryForm=?, maxfilesize=?, jurytime=? WHERE idDepartment=?";
	}

	@Override
	protected String getDeleteSQL() {
		return "DELETE FROM sigesconfig WHERE idDepartment = ?";
	}

	@Override
	protected String getSelectByIdSQL() {
		return "SELECT * FROM sigesconfig WHERE idDepartment = ?";
	}

}