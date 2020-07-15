const PythonShell = require('python-shell').PythonShell,
    multer = require('multer'),
    router = require('express').Router();


//File upload config-----------------------------------------------------------------------------
const storage = multer.diskStorage({
    destination: (req,res,callback)=>{
        callback(null,'Video Uploads/');
    },
    filename : (req,res,callback)=>{
        console.log(req.file);
        callback(null, Date.now()+'.mp4');
    }
});
const fileUpload = multer({ storage : storage });



router.post('/' , fileUpload.single('videoInput'), (req,res)=>{
    console.log(req.file);

    //Python Shell config------------------------------------------------------------------------------
    const pythonShellOptions = {
        mode : "text",
        scriptPath : './',
        args : ['Hello From Node Video Name :' ,Date.now()+'.mp4']
    }
    PythonShell.run('CrowdCountModel.py' , pythonShellOptions, (err,output)=>{
        if(err) throw err;
        console.log(output);
    });

    res.send("Done");
});

module.exports = router;