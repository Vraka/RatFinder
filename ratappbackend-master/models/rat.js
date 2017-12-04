const mongoose = require('mongoose');

const RatSchema = mongoose.Schema({
	Unique_Key:{
		type: Number,
		required: true,
		index: true,
		unique: true
	},
	Created_Date:{
		type: Date,
		required: true,
		Default: Date.now
	},
	Location_Type:{
		type: String,
		required: true,
		enum: [
		'1-2 Family Dwelling',
		'3+ Family Apt. Building',
		'3+ Family Mixed Use Building',
		'Commercial Building',
		'Vacant Lot',
		'Construction Site',
		'Hospital',
		'Catch Basin/Sewer']
	},
	Incident_Zip:{
		type: Number,
		required: true,
		min: 10000,
		max: 99999
	},
	Incident_Address:{
		type: String,
		required: true
	},
	City:{
		type: String,
		required: true
	},
	Borough:{
		type: String,
		required: true,
		enum: [
		'MANHATTAN',
		'STATEN ISLAND',
		'QUEENS',
		'BROOKLYN',
		'BRONX']
	},
	Latitude:{
		type: Number,
		required: true
	},
	Longitude:{
		type: Number,
		required: true
	}
});

const Rat = module.exports = mongoose.model('Rat', RatSchema);