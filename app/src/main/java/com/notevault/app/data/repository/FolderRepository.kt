package com.notevault.app.data.repository

import com.notevault.app.data.local.dao.FolderDao
import com.notevault.app.data.local.entity.FolderEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepository @Inject constructor(
    private val folderDao: FolderDao
) {
    fun getAllFolders(): Flow<List<FolderEntity>> =
        folderDao.getAllFolders()

    suspend fun getFolderById(folderId: Long): FolderEntity? =
        folderDao.getFolderById(folderId)

    suspend fun insertFolder(folder: FolderEntity): Long =
        folderDao.insertFolder(folder)

    suspend fun updateFolder(folder: FolderEntity) =
        folderDao.updateFolder(folder)

    suspend fun deleteFolder(folder: FolderEntity) =
        folderDao.deleteFolder(folder)

    fun getNoteCountForFolder(folderId: Long): Flow<Int> =
        folderDao.getNoteCountForFolder(folderId)

    suspend fun getFolderCount(): Int =
        folderDao.getFolderCount()
}
