"use strict"
let inputFiles = [], fileBlock, startTime, endTime;
const fileinputs = document.getElementsByClassName('fileInput')
    , fileDatas = document.getElementsByClassName('fileData')
    , uploadButtons = document.getElementsByClassName('uploadButton')
    , progressBars = document.getElementsByClassName('progressBar')
    , resumeButtons = document.getElementsByClassName('resumeButton')
    , plusIcons = document.getElementsByClassName('plusIcon')
    , stationNames = document.getElementsByClassName('stationName') 
    , date = new Date(); 


for(let i=0; i<plusIcons.length;i++)
    plusIcons[i].addEventListener('click', (e)=>{
        fileinputs[i].click()
    })

for(let i=0 ; i<fileinputs.length ; i++)
    fileinputs[i].addEventListener('change',(event)=>{
        inputFiles[i] = event.target.files[0];
        fileDatas[i].innerText = " \n Name: "+inputFiles[i].name + " \n Size: "+ inputFiles[i].size/1048676 + " Mb \n Type: "+inputFiles[i].type 
    })

for(let i=0 ; i<uploadButtons.length ; i++)
    uploadButtons[i].addEventListener('click', (e)=>{
        uploadTheFile(i)
    })

const uploadTheFile = (i) =>{
    
    //initialize filereader---------------------------------------------------------------------------
    let fr = new FileReader()
    //asssigning an event handler to FileReaders onload which will get fired every time a block is read
    fr.onload = (e)=>{
        socket.emit('a file block' , {fileName: inputFiles[i].name , fileBlock: fr.result})
    }

    //Connecting sockets-------------------------------------------------------------------------------
    let socket = io.connect(window.location.host)
    startTime = performance.now()

    //Emit Event 1 : start upload----------------------------------------------------------------------
    socket.emit('start upload' , {
        name: inputFiles[i].name, 
        size: inputFiles[i].size, 
        type: inputFiles[i].type, 
        train:{ trainNo: Number(document.getElementById('trainId').value), start: "Virar" , dest:"Churchgate" , current:stationNames[i].innerText , timestamp:date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()}})

    //Listen to Event 1 : send next block--------------------------------------------------------------
    socket.on('send next block' , (data)=>{

        updateProgressBar(Math.ceil(data.start/inputFiles[i].size*100) , i)

        if(data.resume == true){
            resumeButtons[i].style.visibility = 'visible'
            resumeButtons[i].addEventListener('click' , (e)=>{
                readFile(fr , data.start , data.end , i)
                resumeButtons[i].style.visibility = 'hidden'
            })
        }
        else
            readFile(fr , data.start , data.end ,i)
    })

    //Listen to Event 2 : upload completed-------------------------------------------------------------
    socket.on('upload done' , data => {
        if(data.alreadyExisted == true)
            fileDatas[i].innerText += "\n\n File exists "
        socket.emit('close connection',{})
        updateProgressBar(100 , i)
        endTime = performance.now()
        fileDatas[i].innerText += "\n\nTime : "+(endTime - startTime)/1000 +" seconds"
    })

}


const updateProgressBar = (progress , i)=>{
    progressBars[i].innerText = progress+"%"
}

const readFile = (fr , start , end , i) => {
    if(end < inputFiles[i].size)   
        //block end is less than file size
        fileBlock = inputFiles[i].slice(start,end)
    else    
        //end of the block to send is 
        fileBlock = inputFiles[i].slice(start,inputFiles[i].size)
            
    fr.readAsBinaryString(fileBlock)
}