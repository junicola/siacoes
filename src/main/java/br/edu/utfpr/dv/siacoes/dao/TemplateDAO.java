package br.edu.utfpr.dv.siacoes.dao;

import br.edu.utfpr.dv.siacoes.log.UpdateEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class TemplateDAO<T> {

    protected abstract String getInsertSQL();

    protected abstract void setStatement(PreparedStatement stmt, T obj) throws SQLException;

    public final void create(int idUser, T obj) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionDAO.getInstance().getConnection();

            stmt = conn.prepareStatement(getInsertSQL());

            setStatement(stmt, obj);

            stmt.execute();
            new UpdateEvent(conn).registerUpdate(idUser, obj);

        } finally {
            if ((stmt != null) && !stmt.isClosed())
                stmt.close();
            if ((conn != null) && !conn.isClosed())
                conn.close();
        }
    }

    protected abstract String getSelectSQL();

    protected abstract T loadObject(ResultSet rs) throws SQLException;

    public final List<T> read() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<T> objetos = new ArrayList<>();
        try {
            conn = ConnectionDAO.getInstance().getConnection();
            stmt = conn.prepareStatement(getSelectSQL());

            rs = stmt.executeQuery();

            if (rs.next()) {
                objetos.add(this.loadObject(rs));
            }

        } finally {
            if ((rs != null) && !rs.isClosed())
                rs.close();
            if ((stmt != null) && !stmt.isClosed())
                stmt.close();
            if ((conn != null) && !conn.isClosed())
                conn.close();
        }

        return objetos;
    }


    protected abstract String getUpdateSQL();

    public final void update(int idUser, T obj) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionDAO.getInstance().getConnection();

            stmt = conn.prepareStatement(getUpdateSQL());

            setStatement(stmt, obj);

            stmt.execute();
            new UpdateEvent(conn).registerUpdate(idUser, obj);

        } finally {
            if ((stmt != null) && !stmt.isClosed())
                stmt.close();
            if ((conn != null) && !conn.isClosed())
                conn.close();
        }
    }

    protected abstract String getDeleteSQL();

    public final void delete(int idUser, int idObj) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionDAO.getInstance().getConnection();

            stmt = conn.prepareStatement(getDeleteSQL());

            stmt.setInt(1, idObj);

            stmt.execute();
            new UpdateEvent(conn).registerUpdate(idUser, idObj);

        } finally {
            if ((stmt != null) && !stmt.isClosed())
                stmt.close();
            if ((conn != null) && !conn.isClosed())
                conn.close();
        }
    }


    protected abstract String getSelectByIdSQL();

    public final T findById(int idObj) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try{
            conn = ConnectionDAO.getInstance().getConnection();
            stmt = conn.prepareStatement(getSelectByIdSQL());

            stmt.setInt(1, idObj);

            rs = stmt.executeQuery();

            if(rs.next()){
                return this.loadObject(rs);
            }else{
                return null;
            }
        }finally{
            if((rs != null) && !rs.isClosed())
                rs.close();
            if((stmt != null) && !stmt.isClosed())
                stmt.close();
            if((conn != null) && !conn.isClosed())
                conn.close();
        }
    }

    public final void save(int idUser, T config, int idObj) throws SQLException{
        boolean insert = (this.findById(idObj) == null);

        if (insert) {
            create(idUser, config);
        } else {
            update(idUser, config);
        }
    }
}