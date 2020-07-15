let { getCrowdCount } = require("../model/database")

module.exports = async (req, res) => {
    try {
        let crowdCount = await getCrowdCount(req.params.trainNo)
        res.json({ crowdCount: crowdCount })
    } catch (error) {
        res.json({ error: "::" + error })
    }
}

