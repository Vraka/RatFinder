const express = require('express');
const bcrypt = require('bcrypt');
const router = express.Router();
const jwt = require('jsonwebtoken');
const secret = 'secretshhh';

const User = require('../models/user');
const Rat = require('../models/rat');

function verifyUser(token) {
	try {
		var decoded = jwt.verify(token, secret);
		return true;
	} catch(err) {
		return false;
	}
}

function verifyAdmin(token) {
	try {
		var decoded = jwt.verify(token, secret);
		return (decoded.isAdmin);
	} catch(err) {
		return false;
	}
}
router.post('/login', (req, res, next)=>{
	User.find({login_name: req.body.login_name}).exec((err, response)=>{
		if (response && response[0]) {
			if (response[0].isLocked == false) {
				bcrypt.compare(req.body.password, response[0].password, (cryptErr, cryptResponse)=>{
					if (cryptErr) {
						res.json({
							status: 400,
							body: {msg: 'Error comparing passwords'}
						});
					} else {
						if (cryptResponse) {
							if (response[0].login_attempts != 0) {
								User.update({_id: response[0]._id}, {login_attempts: 0});
							}
							jwt.sign({
								login_name: response[0].login_name,
								isAdmin: response[0].isAdmin,
								contact_info: response[0].contact_info
							}, secret, {
								algorithm: 'HS256',
								expiresIn: '7d'
							}, function(jwterr, token) {
								if (jwterr) {
									console.log(jwterr);
									res.json({
										status: 400,
										body: {msg: 'Token creation error'}
									});
								} else {
									res.json({
										status: 200,
										body: {msg: 'Login Successful', token: token}
									});
								}
							});
						} else {
							if (response[0].login_attempts >= 2) {
								User.update({_id: response[0]._id}, {login_attempts: 3, isLocked: true}, (Uerr, raw)=>{
									res.json({
										status: 400,
										body: {msg: 'Incorrect password. Locking account'}
									});
								});
							} else {
								User.update({_id: response[0]._id}, {login_attempts: response[0].login_attempts + 1}, (Uerr, raw)=>{
									res.json({
										status: 400,
										body: {msg: 'Incorrect password'}
									});
								});
							}
						}
					}
				});
			} else {
				res.json({
					status: 400,
					body: {msg: 'Account is locked'}
				});
			}
		} else {
			res.json({
				status: 400,
				body: {msg: 'Cannot find user'}
			});
		}
	});
});

router.post('/register', (req, res, next)=>{
	bcrypt.hash(req.body.password, 10, (err, hash)=>{
		if(err) {
			console.log('Hashing error:'+err);
			res.json({
				status: 400,
				body: {msg: 'Hashing error'}
			});
		} else {
			let newUser = new User({
				login_name: req.body.login_name,
				password: hash,
				contact_info: req.body.contact_info,
				isAdmin: false
			});
			newUser.save((saveErr, user)=>{
				if(saveErr) {
					console.log('Database error'+saveErr);
					res.json({
						status: 400,
						body: {msg: 'Database error'}
					});
 				} else {
 					jwt.sign({
								login_name: user.login_name,
								isAdmin: user.isAdmin,
								contact_info: user.contact_info
							}, secret, {
								algorithm: 'HS256',
								expiresIn: '7d'
							}, function(jwterr, token) {
								if (jwterr) {
									console.log(jwterr);
									res.json({
										status: 400,
										body: {msg: 'Token creation error'}
									});
								} else {
									res.json({
										status: 200,
										body: {msg: 'Register Successful', token: token}
									});
								}
					});
 				}
			})
		}
	});

});

router.post('/addRat', (req, res, next)=>{
	if (verifyUser(req.body.token)) {
		Rat.findOne().sort('-Unique_Key').exec(function(err, item) {
		if (err) {
			console.log('Cannot find max Unique_Key');
			res.json({
				status: 400,
				body: {msg: 'Cannot find max Unique_Key'}
			});
		} else {
			let newRat = new Rat({
				Unique_Key: (item != null?item.Unique_Key:0) + 1,
				Created_Date: req.body.Created_Date,
				Location_Type: req.body.Location_Type,
				Incident_Zip: req.body.Incident_Zip,
				Incident_Address: req.body.Incident_Address,
				City: req.body.City,
				Borough: req.body.Borough,
				Latitude: req.body.Latitude,
				Longitude: req.body.Longitude
			});
			newRat.save((saveErr, rat)=>{
				if (saveErr) {
					console.log('Database error'+saveErr);
					res.json({
						status: 400,
						body: {msg: 'Database error'}
					});
				} else {
					console.log('Rat added successfully');
					res.json({
						status: 200,
						body: {msg: 'Rat added successfully',
						data: rat}
					});
				}
			});
		}
	});
	} else {
		res.json({
			status: 401,
			body: {msg: 'Requires a valid token to call this method'}
		});
	}

	

});

router.get('/rats/:token', (req, res, next)=>{
	if (verifyUser(req.params.token)) {
	Rat.find(function(err, rats){
		if (err) {
			res.json({
				status: 400,
				body: {msg: 'Unexpected Error'}
			});
		} else {
			res.json({
				status: 200,
				body: {data: rats}
			});
		}

	});
	} else {
		res.json({
			status: 401,
			body: {msg: 'Requires a valid token to call this method'}
		});
	}
});

router.post('/search/dateRange', (req, res, next)=>{
	if (verifyUser(req.body.token)) {
	Rat.find({Created_Date: { $gt: req.body.Start_Date, $lt: req.body.End_Date}}).exec((err, response)=>{
		if (err) {
			res.json({
				status: 400,
				body: {msg: 'Date Search Error'}
			});
		} else {
						res.json({
				status: 200,
				body: {data: response}
			});
		}
	});
	} else {
		res.json({
			status: 401,
			body: {msg: 'Requires a valid token to call this method'}
		});
	}
})

router.post('/search/Borough', (req, res, next)=>{
	if (verifyUser(req.body.token)) {
	Rat.find({Borough: req.body.Borough}).exec((err, response)=>{
		if (err) {
			console.log('Borough search Error');
			res.json({
				status: 400,
				body: {msg: 'Search failed'}
			});
		} else {
			res.json({
				status: 200,
				body: {data: response}
			});
		}
	});
	} else {
		res.json({
			status: 401,
			body: {msg: 'Requires a valid token to call this method'}
		});
	}
});

router.post('/search/Incident_Zip', (req, res, next)=>{
	if (verifyUser(req.body.token)) {
	Rat.find({Incident_Zip: req.body.Incident_Zip}).exec((err, response)=>{
		if (err) {
			console.log('Incident Zip search error');
			res.json({
				status: 400,
				body: {msg: 'Search failed'}
			});
		} else {
			res.json({
				status: 200,
				body: {data: response}
			});
		}
	});
	} else {
		res.json({
			status: 401,
			body: {msg: 'Requires a valid token to call this method'}
		});
	}
});

router.post('/search/Location_Type', (req, res, next)=>{
	if (verifyUser(req.body.token)) {
	Rat.find({Location_Type: req.body.Location_Type}).exec((err, response)=>{
		if (err) {
			console.log('Location Type search failed');
			res.json({
				status: 400,
				body: {msg: 'Search failed'}
			});
		} else {
			res.json({
				status: 200,
				body: {data: response}
			});
		}
	});
	} else {
		res.json({
			status: 401,
			body: {msg: 'Requires a valid token to call this method'}
		});
	}
});
//get users
router.post('/users', (req, res, next)=>{
	var temp = verifyAdmin(req.body.token);
	console.log(temp);
	if (verifyAdmin(req.body.token)) {
	User.find(function(err, users){
		if (err) {
			res.json({
				status: 400,
				body: {msg: 'Search error'}
			});
		} else {
			res.json({
				status: 200,
				body: {data: users}
			});
		}

	});
	} else {
		res.json({
			status: 403,
			body: {msg: 'Requires a valid admin to call this method'}
		});
	}
});

//lock user
router.post('/lock', (req, res, next)=>{
	if (verifyAdmin(req.body.token)) {
	User.update({_id: req.body.id}, {isLocked: true}, (err, raw)=>{
		if (err) {
			res.json({
				status: 400,
				body: {msg: 'Locking account error'}
			});
		} else {
			res.json({
				status: 200,
				body: {msg: 'Locked account'}
			});
		}
	});
	} else {
		res.json({
			status: 403,
			body: {msg: 'Requires a valid admin to call this method'}
		});
	}
});

//unlock user
router.post('/unlock', (req, res, next)=>{
	if (verifyAdmin(req.body.token)) {
	User.update({_id: req.body.id}, {isLocked: false}, (err, raw)=>{
		if (err) {
			res.json({
				status: 400,
				body: {msg: 'Unlocking account error'}
			});
		} else {
			res.json({
				status: 200,
				body: {msg: 'Unlocked account successfully'}
			});
		}
	});
	} else {
		res.json({
			status: 403,
			body: {msg: 'Requires a valid admin to call this method'}
		});
	}
});

//remove user
router.post('/deleteUser', (req, res, next)=>{
	if (verifyAdmin(req.body.token)) {
	User.remove({_id: req.body.id}, (err, result)=>{
		if (err) {
			console.log('Error removing user');
			res.json({
				status: 400,
				body: {msg: 'Error removing user'}
			});
		} else {
			res.json({
				status: 200,
				body: {msg: 'Removed user'}
			});
		}
	});
	} else {
		res.json({
			status: 403,
			body: {msg: 'Requires a valid admin to call this method'}
		});
	}
});

router.post('/createUser', (req, res, next)=>{
	if (verifyAdmin(req.body.token)) {
		bcrypt.hash(req.body.password, 10, (err, hash)=>{
			if(err) {
				console.log('Hashing error:'+err);
				res.json({
					status: 400,
					body: {msg: 'Hashing error'}
				});
			} else {
				let newUser = new User({
					login_name: req.body.login_name,
					password: hash,
					contact_info: req.body.contact_info,
					isAdmin: req.body.isAdmin
				});
				newUser.save((saveErr, user)=>{
					if(saveErr) {
						console.log('Database error'+saveErr);
						res.json({
							status: 400,
							body: {msg: 'Database error'}
						});
					} else {
						res.json({
							status: 200,
							body: {msg: 'Register Successful', data: user}
						});
					}
				});
			}
		})
	} else {
		res.json({
			status: 403,
			body: {msg: 'Requires a valid admin to call this method'}
		});
	}
});

module.exports = router;