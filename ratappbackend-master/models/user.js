const mongoose = require('mongoose');

const ContactSchema = mongoose.Schema({
	login_name:{
		type: String,
		required: true,
		index: true,
		unique: true
	},
	password:{
		type: String,
		required: true
	},
	isLocked:{
		type: Boolean,
		required: true,
		default: false
	},
	contact_info:{
		type: String,
		required: true
	},
	isAdmin:{
		type: Boolean,
		required: true
	},
	login_attempts:{
		type: Number,
		required: true,
		default: 0,
		min: 0
	}
});

const Contact = module.exports = mongoose.model('Users', ContactSchema);