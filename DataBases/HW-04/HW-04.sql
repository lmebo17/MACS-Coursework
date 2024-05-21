SELECT 
    department_name,
CASE
    WHEN COUNT(employee_id) = 0 THEN 'N/A'
    ELSE TO_CHAR(COUNT(EMPLOYEE_ID)) END AS emp_cnt,
CASE
    WHEN COUNT(DISTINCT job_id) = 0 THEN 'N/A'
    ELSE TO_CHAR(COUNT(DISTINCT job_id)) END AS job_cnt,
CASE
    WHEN SUM(salary) IS NULL THEN 'N/A'
    ELSE TO_CHAR(SUM(salary)) END AS sum_sal,
CASE
    WHEN (MIN(salary) + MAX(salary)) IS NULL THEN 'N/A'
    ELSE TO_CHAR(MIN(salary) + MAX(salary)) END AS sum_min_max,
LISTAGG(PHONE_NUMBER, ', ') WITHIN GROUP(ORDER BY PHONE_NUMBER) AS PHONE_NUMBERS
FROM
    departments
LEFT JOIN
    employees ON departments.department_id = employees.department_id
LEFT JOIN
    locations ON departments.location_id = locations.location_id
LEFT JOIN
    countries ON locations.country_id = countries.country_id
WHERE
    department_name NOT LIKE '% %'
    AND UPPER(countries.country_name) LIKE CONCAT(countries.country_id, '%')
GROUP BY
    department_name,
    countries.country_id,
    countries.country_name
ORDER BY
    countries.COUNTRY_ID,
    countries.COUNTRY_NAME DESC;