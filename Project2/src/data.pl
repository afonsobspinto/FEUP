subjects([
%Subject[ID, Name, AreaID, Semester, [Hours Per Theoretical], [Hours Per Pratical]]
[1, 'PLOG', 1, 1, [1, 1], [2, 2, 2, 2, 2, 2]]
]).

teachers([
%Teacher[ID, Name, Category, Area, Preference]
[1, 'HLC', 'Assistant', 1, 0],
[2, 'RCS', 'Associated', 1, 0],
[3, 'DCS', 'Assistant', 1, 0],
[4, 'AAS', 'Associated', 2, 0],
[5, 'AVC', 'Assistant', 2, 0],
[6, 'JGB', 'Assistant', 2, 0],
[7, 'RPR', 'Assistant', 2, 0],
[8, 'AMA', 'Assistant', 3, 0],
[9, 'TBS', 'Assistant', 3, 0],
[10, 'NHF', 'Assistant', 3, 0]
]).

scientificArea([
[1, 'Programming'],
[2, 'Multimedia'],
[3, 'Engineering'],
[4, 'Systems'],
[5, 'Information'],
[6, 'AI'],
[7, 'Social']
]).
