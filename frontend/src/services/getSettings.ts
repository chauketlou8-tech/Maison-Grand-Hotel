import axios from "axios";

export default async function getSettings() {
    try{
        const { data } = await axios.get("/api/v1/settings");
        return data.data;
    }

    catch(error){
        console.log(error);
    }
}