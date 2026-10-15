@echo off

set PGPASSWORD=TemaSecondary0909@
"C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -h localhost -p 3001 -d masongrandhotel -f reset.sql

npx tsx -e "(async () => { const { connectRedis } = await import('../backend/src/config/cache.ts'); const c = await connectRedis(); await c.flushDb(); console.log('flushed'); process.exit(0); })();"