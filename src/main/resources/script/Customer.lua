-- KEYS[1] target hash key
-- KEYS[2] target status hash key
-- ARGV[n >= 3] current timestamp, max count, duration, max count, duration, ...

-- HASH: KEYS[1]
--   field:ct(count)
--   field:lt(limit)
--   field:dn(duration)
--   field:rt(reset)

local res = {}
--local policyCount = (#ARGV - 1) / 2
local limit = redis.call('hmget', KEYS[1], 'ct', 'lt', 'dn', 'rt')

if limit[1] then

  res[1] = tonumber(limit[1]) - 1
  res[2] = tonumber(limit[2])
  res[3] = tonumber(limit[3]) or ARGV[3]
  res[4] = tonumber(limit[4])

  --if policyCount > 1 and res[1] == -1 then
  --  redis.call('incr', KEYS[2])
  --  redis.call('pexpire', KEYS[2], res[3] * 2)
  --  local index = tonumber(redis.call('get', KEYS[2]))
  --  if index == 1 then
  --    redis.call('incr', KEYS[2])
  --  end
  --end

  -- 由于17行减一理论是 0
  if res[1] > -1 then
    redis.call('hincrby', KEYS[1], 'ct', -1)
  else
    res[1] = -1
  end

else

  local index = 1
--  if policyCount > 1 then
--    index = tonumber(redis.call('get', KEYS[2])) or 1
--    if index > policyCount then
--      index = policyCount
--    end
--  end

  local total = tonumber(ARGV[index * 2])
  res[1] = total - 1 -- 获取一个令牌
  res[2] = total -- 最大令牌数
  res[3] = tonumber(ARGV[index * 2 + 1]) -- 缓存时间段durable
  res[4] = tonumber(ARGV[1]) + res[3] --过期时间

  redis.call('hmset', KEYS[1], 'ct', res[1], 'lt', res[2], 'dn', res[3], 'rt', res[4])
  redis.call('pexpire', KEYS[1], res[3]) --单位是毫秒

end

return res[1] --判断 >= 0 说明获取令牌成功
