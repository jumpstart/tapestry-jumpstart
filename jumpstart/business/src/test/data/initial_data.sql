-- ------------------------------------------------------------------------------------------------------------
-- INITIAL DATA FOR JUMPSTART
-- ------------------------------------------------------------------------------------------------------------

-- persons

delete from Person;

insert into Person(id, version, firstName, lastName, region, startDate)
	values (1, 0, 'Humpty', 'Dumpty', 'EAST_COAST', '2007-12-05');
insert into Person(id, version, firstName, lastName, region, startDate)
	values (2, 0, 'Mary', 'Contrary', 'EAST_COAST', '2008-02-29');
insert into Person(id, version, firstName, lastName, region, startDate)
	values (3, 0, 'Jack', 'Sprat', 'WEST_COAST', '2007-02-28');
insert into Person(id, version, firstName, lastName, region, startDate)
	values (4, 0, 'Jill', 'Spill', 'WEST_COAST', '2008-02-29');
insert into Person(id, version, firstName, lastName, region, startDate)
	values (5, 0, 'Dishy', 'Spoon', 'EAST_COAST', '2008-02-29');

-- date stuff

delete from DatesExample;

insert into DatesExample(id, version, aDate, aTime, aTimestamp, 
			aDateTime, aDateTimeWithTZ, aDateTimeTZ, aDateMidnight, aDateMidnightWithTZ, aDateMidnightTZ,
			aLocalDateTime, aLocalDate, aLocalTimeAsTime, aLocalTimeAsMillis, aLocalTimeAsString)
	values (1, 0, '2001-07-31', '10:35:17', '2001-07-31 10:35:17', 
			'2001-07-31 10:35:17', '2001-07-31 10:35:17', 'GMT+10:00', '2001-07-31', '2001-07-31', 'GMT+10:00',
			'2001-07-31 10:35:17', '2001-07-31', '10:35:17', 2117000, '10:35:17.000');

-- users

delete from UserRole;
delete from Role;
delete from Users;
delete from UserPassword;

insert into UserPassword (id, version, password)
	values	(1, 0, 'secofr');
insert into Users (id, version, loginId, salutation, firstName, lastName, emailAddress, expiryDate, active, userPasswordId,
	pageStyle, dateInputPattern, dateViewPattern, dateListPattern)
	values	(1, 0, 'secofr', 'Ms', 'Security', 'Officer', 'secofr@thecompany.com', null, true, 1,
		0, 'dd/MM/yyyy', 'dd MMM yyyy', 'yyyy-MM-dd');
insert into UserPassword (id, version, password)
	values	(2, 0, 'admin');
insert into Users (id, version, loginId, salutation, firstName, lastName, emailAddress, expiryDate, active, userPasswordId,
	pageStyle, dateInputPattern, dateViewPattern, dateListPattern)
	values	(2, 0, 'admin', '', 'The', 'Administrator', 'admin@thecompany.com', null, true, 2,
		1, 'dd/MM/yyyy', 'dd MMM yyyy', 'yyyy-MM-dd');
insert into UserPassword (id, version, password)
	values	(3, 0, 'john');
insert into Users (id, version, loginId, salutation, firstName, lastName, emailAddress, expiryDate, active, userPasswordId,
	pageStyle, dateInputPattern, dateViewPattern, dateListPattern)
	values	(3, 0, 'john', 'Mr', 'John', 'Citizen', 'john@thecompany.com', '2010-12-31', true, 3,
		1, 'dd/MM/yyyy', 'dd MMM yyyy', 'yyyy-MM-dd');
		
-- roles
		
insert into Role(id, name, version)
	values	(1, 'Security Officer', 0);
insert into Role(id, name, version)
	values	(2, 'Administration', 0);
insert into Role(id, name, version)
	values	(3, 'Accounts', 0);

-- user roles

insert into UserRole(id, userId, roleId, active, version)
	values	(1, 1, 1, true, 0);
insert into UserRole(id, userId, roleId, active, version)
	values	(2, 2, 2, true, 0);
insert into UserRole(id, userId, roleId, active, version)
	values	(3, 2, 3, true, 0);
insert into UserRole(id, userId, roleId, active, version)
	values	(4, 3, 3, true, 0);

-- categories - a partial list of the Dewey Decimal Classification codes

delete from Classification;
	
insert into Classification (id, version, parentId, label) values						
(1000, 0, null, '000-099 - Computer science, information & general works'),
(2000, 0, 1000, '000 Computer science, knowledge & systems'),
(3000, 0, 2000, '000 Computer science, knowledge & general works'),
(3001, 0, 2000, '001 Knowledge'),
(3002, 0, 2000, '002 The book (i.e. Meta writings about books)'),
(3003, 0, 2000, '003 Systems'),
(3004, 0, 2000, '004 Data processing & computer science'),
(3005, 0, 2000, '005 Computer programming, programs & data'),
(3006, 0, 2000, '006 Special computer methods'),
(3007, 0, 2000, '007 [Unassigned]'),
(3008, 0, 2000, '008 [Unassigned]'),
(3009, 0, 2000, '009 [Unassigned]'),
(2010, 0, 1000, '010 Bibliographies'),
(3010, 0, 2010, '010 Bibliography'),
(3011, 0, 2010, '011 Bibliographies'),
(3012, 0, 2010, '012 Bibliographies of individuals'),
(3013, 0, 2010, '013 [Unassigned]'),
(3014, 0, 2010, '014 Of anonymous & pseudonymous works'),
(3015, 0, 2010, '015 Bibliographies of works from specific places'),
(3016, 0, 2010, '016 Bibliographies of works on specific subjects'),
(3017, 0, 2010, '017 General subject catalogs'),
(3018, 0, 2010, '018 Catalogs arranged by author, date, etc.'),
(3019, 0, 2010, '019 Dictionary catalogs'),
(2020, 0, 1000, '020 Library & information sciences'),
(3020, 0, 2020, '020 Library & information sciences'),
(3021, 0, 2020, '021 Library relationships'),
(3022, 0, 2020, '022 Administration of physical plant'),
(3023, 0, 2020, '023 Personnel management'),
(3024, 0, 2020, '024 No longer used - formerly Regulations for readers'),
(3025, 0, 2020, '025 Library operations'),
(3026, 0, 2020, '026 Libraries for specific subjects'),
(3027, 0, 2020, '027 General libraries'),
(3028, 0, 2020, '028 Reading & use of other information media'),
(3029, 0, 2020, '029 No longer used - formerly Literary methods'),
(2030, 0, 1000, '030 Encyclopedias & books of facts'),
(3030, 0, 2030, '030 General encyclopedic works'),
(3031, 0, 2030, '031 Encyclopedias in American English'),
(3032, 0, 2030, '032 Encyclopedias in English'),
(3033, 0, 2030, '033 In other Germanic languages'),
(3034, 0, 2030, '034 Encyclopedias in French, Occitan & Catalan'),
(3035, 0, 2030, '035 In Italian, Romanian & related languages'),
(3036, 0, 2030, '036 Encyclopedias in Spanish & Portuguese'),
(3037, 0, 2030, '037 Encyclopedias in Slavic languages'),
(3038, 0, 2030, '038 Encyclopedias in Scandinavian languages'),
(3039, 0, 2030, '039 Encyclopedias in other languages'),
(2040, 0, 1000, '040 [all unassigned] formerly Collected essays by language'),
(3040, 0, 2040, '040 [Unassigned]'),
(3041, 0, 2040, '041 [Unassigned]'),
(3042, 0, 2040, '042 [Unassigned]'),
(3043, 0, 2040, '043 [Unassigned]'),
(3044, 0, 2040, '044 [Unassigned]'),
(3045, 0, 2040, '045 [Unassigned]'),
(3046, 0, 2040, '046 [Unassigned]'),
(3047, 0, 2040, '047 [Unassigned]'),
(3048, 0, 2040, '048 [Unassigned]'),
(3049, 0, 2040, '049 [Unassigned]'),
(2050, 0, 1000, '050 Magazines, journals & serials'),
(3050, 0, 2050, '050 General serial publications'),
(3051, 0, 2050, '051 Serials in American English'),
(3052, 0, 2050, '052 Serials in English'),
(3053, 0, 2050, '053 Serials in other Germanic languages'),
(3054, 0, 2050, '054 Serials in French, Occitan & Catalan'),
(3055, 0, 2050, '055 In Italian, Romanian & related languages'),
(3056, 0, 2050, '056 Serials in Spanish & Portuguese'),
(3057, 0, 2050, '057 Serials in Slavic languages'),
(3058, 0, 2050, '058 Serials in Scandinavian languages'),
(3059, 0, 2050, '059 Serials in other languages'),
(2060, 0, 1000, '060 Associations, organizations & museums'),
(3060, 0, 2060, '060 General organizations & museology'),
(3061, 0, 2060, '061 Organizations in North America'),
(3062, 0, 2060, '062 Organizations in British Isles; in England'),
(3063, 0, 2060, '063 Organizations in central Europe; in Germany'),
(3064, 0, 2060, '064 Organizations in France & Monaco'),
(3065, 0, 2060, '065 Organizations in Italy & adjacent islands'),
(3066, 0, 2060, '066 In Iberian Peninsula & adjacent islands'),
(3067, 0, 2060, '067 Organizations in eastern Europe; in Russia'),
(3068, 0, 2060, '068 Organizations in other geographic areas'),
(3069, 0, 2060, '069 Museum science'),
(2070, 0, 1000, '070 News media, journalism & publishing'),
(3070, 0, 2070, '070 News media, journalism & publishing'),
(3071, 0, 2070, '071 Newspapers in North America'),
(3072, 0, 2070, '072 Newspapers in British Isles; in England'),
(3073, 0, 2070, '073 Newspapers in central Europe; in Germany'),
(3074, 0, 2070, '074 Newspapers in France & Monaco'),
(3075, 0, 2070, '075 Newspapers in Italy & adjacent islands'),
(3076, 0, 2070, '076 In Iberian Peninsula & adjacent islands'),
(3077, 0, 2070, '077 Newspapers in eastern Europe; in Russia'),
(3078, 0, 2070, '078 Newspapers in Scandinavia'),
(3079, 0, 2070, '079 Newspapers in other geographic areas'),
(2080, 0, 1000, '080 General collections'),
(3080, 0, 2080, '080 General collections'),
(3081, 0, 2080, '081 Collections in American English'),
(3082, 0, 2080, '082 Collections in English'),
(3083, 0, 2080, '083 Collections in other Germanic languages'),
(3084, 0, 2080, '084 Collections in French, Occitan & Catalan'),
(3085, 0, 2080, '085 In Italian, Romanian & related languages'),
(3086, 0, 2080, '086 Collections in Spanish & Portuguese'),
(3087, 0, 2080, '087 Collections in Slavic languages'),
(3088, 0, 2080, '088 Collections in Scandinavian languages'),
(3089, 0, 2080, '089 Collections in other languages'),
(2090, 0, 1000, '090 Manuscripts & rare books'),
(3090, 0, 2090, '090 Manuscripts & rare books'),
(3091, 0, 2090, '091 Manuscripts'),
(3092, 0, 2090, '092 Block books'),
(3093, 0, 2090, '093 Incunabula'),
(3094, 0, 2090, '094 Printed books'),
(3095, 0, 2090, '095 Books notable for bindings'),
(3096, 0, 2090, '096 Books notable for illustrations'),
(3097, 0, 2090, '097 Books notable for ownership or origin'),
(3098, 0, 2090, '098 Prohibited works, forgeries & hoaxes'),
(3099, 0, 2090, '099 Books notable for format'),
(1100, 0, null, '100-199 - Philosophy and psychology'),
(2100, 0, 1100, '100 Philosophy'),
(2110, 0, 1100, '110 Metaphysics'),
(2120, 0, 1100, '120 Epistemology'),
(2130, 0, 1100, '130 Parapsychology and occultism'),
(2140, 0, 1100, '140 Philosophical schools of thought'),
(2150, 0, 1100, '150 Psychology'),
(2160, 0, 1100, '160 Logic'),
(2170, 0, 1100, '170 Ethics (Moral philosophy)'),
(2180, 0, 1100, '180 Ancient, medieval, and Eastern philosophy'),
(2190, 0, 1100, '190 Modern Western philosophy (19th-century, 20th-century)'),
(1200, 0, null, '200-299 - Religion'),
(1300, 0, null, '300-399 - Social sciences'),
(1400, 0, null, '400-499 - Language'),
(1500, 0, null, '500-599 - Science'),
(1600, 0, null, '600-699 - Technology'),
(1700, 0, null, '700-799 - Arts'),
(1800, 0, null, '800-899 - Literature'),
(1900, 0, null, '900-999 - History, geography, (& biography)');
	
commit;
