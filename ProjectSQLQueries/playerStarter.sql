SELECT p_playerName
FROM Players, PlayerStats
WHERE p_playerID = p_playerID
GROUP BY p_playerName
HAVING MAX(p_starter)