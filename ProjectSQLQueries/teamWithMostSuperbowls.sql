SELECT t_name
FROM Teams, TeamStats
WHERE t_teamID = ts_teamID
GROUP BY t_name
HAVING MAX(ts_superbowls)