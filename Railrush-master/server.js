const express = require('express')
    , app = express()
    , cors = require('cors')
    , server = require('http').Server(app)     //Server(requestListener?: RequestListener);
    , io = require('socket.io')(server)
    , bodyParser = require('body-parser')
    , path = require('path')
    , fileUpload = require('./controller/fileUpload')
    , crowdCount = require('./controller/crowdCount');

//Creating express app----------------------------------------------------------------------------
app.use(bodyParser.urlencoded({extended:true}));
app.use(express.static('public'))
app.use(cors())


//Routes -------------------------------------------------------------------------------------------
app.get('/',(req,res)=>{
    res.sendFile(path.join(__dirname , '/public/html/Homepage.html'));
});

fileUpload(io)

app.get('/crowdCount/:trainNo',crowdCount)
app.get('/*',(req,res)=>{
    res.send("You've got to a wrong place")
})

//Starting Server ------------------------------------------------------------------------------------
server.listen(process.env.PORT || 80,()=>{
    console.log("Listening"+require('os').cpus().length);
})
