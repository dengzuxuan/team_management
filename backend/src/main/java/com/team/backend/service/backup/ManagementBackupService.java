package com.team.backend.service.backup;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.BackupRemarkType;

/**
 * <p>
 *  TODO
 * </p>
 *
 * @author Colin
 * @since 2023/12/1
 */
public interface ManagementBackupService {
    Result addBackup(BackupRemarkType backupRemarkinfo);

    Result addRecover(String version);
}
