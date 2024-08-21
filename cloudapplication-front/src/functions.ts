'use server';
import api from "./utils/Axios/Axios";

export const postTask = async (data: FormData) => {
    const taskDto = {
      titulo: data.get("titulo"),
    }
    const response = await api.post("/task", taskDto)
    return response.data;
}
export const postFile = async (image: any, idTask: number) => {
    const formData = new FormData();
    formData.append("multipartFile", image.target.files[0])
    const response = await api.post(`/file/${idTask}`, formData)
    return response.data;
}
export  const getFile = async (idTask: number) => {
    const response = await api.get(`/file/${idTask}`)
    return response.data;
}
export const deleteFile = async(idFile : number)=>{
    const response = await api.delete(`/file/${idFile}`)
    return response.data;
}
export const deleteTask = async (id: number) => {
    const response = await api.delete(`/task/${id}`)
    return response.data;
}
export const getTask = async () => {
    const response = await api.get("/task")
    return response.data;
}