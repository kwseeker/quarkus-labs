package top.kwseeker.market.infrastructure.quarkus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import top.kwseeker.market.types.enums.ResponseCode;
import top.kwseeker.market.types.exception.AppException;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ApplicationScoped
public class TransactionTemplate {

    @Inject
    TransactionManager transactionManager;

    // runnable.run() 中可以捕获异常用于打印日志等等，但是捕获后打印完日志需要重新抛出异常
    @SneakyThrows
    public void execute(Runnable runnable) {
        try {
            transactionManager.begin();
            runnable.run();
            transactionManager.commit();
        } catch (Exception e) {
            ResponseCode code = ResponseCode.SYSTEM_ERROR;
            try {
                if (e instanceof PersistenceException
                        && e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                    code = ResponseCode.INDEX_DUP;
                }
                transactionManager.rollback();
                log.warn("Transaction rollbacked, cause: ", e);
            } catch (SystemException ex) {
                log.error("Transaction rollback failed: ", ex);
            }
            if (e instanceof AppException) {
                throw e;
            } else {
                throw new AppException(code.getCode(), e);
            }
        }
    }
}
