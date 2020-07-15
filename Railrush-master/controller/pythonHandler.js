let {updateCrowdInfo , getCrowdCount} = require("../model/database")
    , path = require('path')
    , fs = require('fs')
    , {spawn} = require('child_process')

const callScript = (fileName , trainInfo)=>{
    let process = spawn("python" , [path.join(__dirname , "/../ML Model/CrowdCountModel.py") , fileName])
    process.stdout.on('data', (data)=>{
        let s = data.toString()
        console.log("Model "+s.split(",")[0]+s.split(",")[1]+s.split(",")[2])
        calculateCrowdCount(trainInfo , Number(s.split(",")[0]) , Number(s.split(",")[1]) , Number(s.split(",")[2]))
        fs.rmdir(path.join(__dirname , "/../ML Model/data4/"+fileName),{recursive:true},(err)=>{
            if(err)
                console.log('Cant delete dir')
        })
        fs.rmdir(path.join(__dirname , "/../Video Uploads/"+fileName),{recursive:true},(err)=>{
            if(err)
                console.log('Cant delete dir')
        })
    })
    process.stderr.on('data' , (data)=>{
        console.log(data.toString())
    })
    process.on('close',(exitCode)=>{
        console.log("Exit : "+exitCode)
    })
}

const calculateCrowdCount = async (trainInfo,max,min,alreadyPT)=>{
    try {
        let crowdCount = await getCrowdCount(trainInfo.trainNo)
        saveToDatabase(trainInfo , crowdCount+2*alreadyPT - max - min)    
    } catch (e) {
        console.log(e)
    }
}

const saveToDatabase = (trainInfo , crowdCount)=>{
    updateCrowdInfo(trainInfo.trainNo , {current:trainInfo.current , crowdCount:crowdCount , timestamp: trainInfo.timestamp})
        .then((result)=>{
            console.log("DB stats n : "+result.result.n)
        }).catch((e)=>{
            console.log(e)
        })
}

module.exports = callScript