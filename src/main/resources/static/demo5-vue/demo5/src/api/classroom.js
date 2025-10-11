import axios from 'axios'
// 获取分页数据
export const getPage = (page = 0) =>
    axios.get('http://localhost:9090/api/classroom/page', { params: { page } })
        .then(res => res.data.data)   // 你的统一返回是 ResponseDto

//增加教室
export const createClassroom = (form) =>
    axios.post('http://localhost:9090/api/classroom', form)
        .then(res => res.data.data)

//删除教室
export const deleteClassroom = (id) =>
    axios.delete(`http://localhost:9090/api/classroom/${id}`)
        .then(res => res.data.data)
//修改教室
export const updateClassroom = (id, form) =>
    axios.put(`http://localhost:9090/api/classroom/${id}`, form)
        .then(res => res.data.data)


//查询教室
//export const getClassroom = (id) =>
 //   axios.get(`http://localhost:9090/api/classroom/${id}`)
   //     .then(res => res.data.data)



//增加教室图片
export const createClassroomImage = (id, file) =>
{
    const formdata = new FormData();
    formdata.append("image", file);
    return   axios.post(
        `http://localhost:9090/api/classroom/${id}/image`, formdata, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    )

}

//修改图片
export const updateClassroomImage = (id, file) =>
{
    const formdata = new FormData();
    formdata.append("image", file);
    return   axios.put(
        `http://localhost:9090/api/classroom/${id}/image`, formdata, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    )

}



//预约





//取消预约