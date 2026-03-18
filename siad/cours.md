```sql
select Gare from Ligne T1 
    where rang > (select rang from Ligne T2 where T1.NO = T2.NO and Gare = 'Aix') 
    and rang < (select rang from Ligne T3 where T1.NO = T3.NO and Gare = 'Lille');
```