//package app.messages.jdbc;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.jdbc.datasource.DataSourceUtils;
//import org.springframework.stereotype.Component;
//
//import javax.sql.DataSource;
//import java.sql.*;
//import java.time.temporal.ChronoField;
//
//@Component
//public class MessageRepository {
//
//    private DataSource dataSource;
//
//    public MessageRepository(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    private final static Log log = LogFactory.getLog(MessageRepository.class);
//
//    public Message saveMessage(Message message) {
//        log.info("Saved message: " + message.getText());
//
//        Connection c = DataSourceUtils.getConnection(dataSource);
//        try {
//            String insertSql = "INSERT INTO messages (`id`, `text`, `created_date`) VALUE (null, ?, ?)";
//            PreparedStatement ps = c.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
//            ps.setString(1, message.getText());
//            ps.setTimestamp(2, new Timestamp(message.getCreatedDate().getLong(ChronoField.MILLI_OF_SECOND)));
//            int rowsAffected = ps.executeUpdate();
//            if (rowsAffected > 0) {
//                // 새로 저장된 메시지 id 가져오기
//                ResultSet result = ps.getGeneratedKeys();
//                if (result.next()) {
//                    int id = result.getInt(1);
//                    return new Message(id, message.getText(), message.getCreatedDate());
//                } else {
//                    log.error("Failed to retrieve id. No row in result set");
//                    return null;
//                }
//            } else {
//                // insert 실패
//                return null;
//            }
//        } catch (SQLException ex) {
//            log.error("Failed to save message", ex);
//            try {
//                c.close();
//            } catch (SQLException e) {
//                log.error("Failed to close connection", e);
//            }
//        } finally {
//            DataSourceUtils.releaseConnection(c, dataSource);
//        }
//        return null;
//    }
//}
