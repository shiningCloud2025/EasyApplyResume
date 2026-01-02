import request from '@/utils/request'

/**
 * 文件上传 API
 */
export const fileApi = {
  /**
   * 上传管理员头像
   * @param file 文件对象
   * @param adminId 管理员ID
   * @returns 文件URL
   */
  uploadAdminAvatar(file: File, adminId: number) {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('adminId', adminId.toString())
    
    return request.post<string>('/admin/file/uploadAdminHeadImg', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  /**
   * 删除文件（公有）
   * @param fileUrl 文件URL
   */
  deleteFile(fileUrl: string) {
    return request.delete('/admin/file/deleteFile', {
      params: { fileUrl }
    })
  },

  /**
   * 查询管理员的所有头像
   * @param adminId 管理员ID
   */
  listAdminAvatars(adminId: number) {
    return request.get<string[]>('/admin/file/listFilesByAdminId', {
      params: { adminId }
    })
  }
}
